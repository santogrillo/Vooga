package networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import engine.AbstractGameController;
import engine.play_engine.PlayController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.CreateGameRoom;
import networking.protocol.PlayerClient.DeleteElement;
import networking.protocol.PlayerClient.JoinRoom;
import networking.protocol.PlayerClient.LoadLevel;
import networking.protocol.PlayerClient.MoveElement;
import networking.protocol.PlayerClient.PlaceElement;
import networking.protocol.PlayerServer.Game;
import networking.protocol.PlayerServer.GameRoomCreationStatus;
import networking.protocol.PlayerServer.GameRoomJoinStatus;
import networking.protocol.PlayerServer.GameRoomLaunchStatus;
import networking.protocol.PlayerServer.GameRooms;
import networking.protocol.PlayerServer.Games;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Notification;
import networking.protocol.PlayerServer.NumberOfLevels;
import networking.protocol.PlayerServer.PlayerExited;
import networking.protocol.PlayerServer.PlayerJoined;
import networking.protocol.PlayerServer.PlayerNames;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;

public abstract class AbstractServerController {

	// TODO - Move to resources file
	private final String ERROR_UNAUTHORIZED = "You do not belong to any game room";
	private final String ERROR_CLIENT_ENGAGED = "You are already in another game room";
	private final String ERROR_NONEXISTENT_ROOM = "This game room does not exist";
	private final String GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME = "This game does not exist";
	private final String GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN = "This username has already been taken for this game room";
	private final String LOAD_LEVEL_ERROR_NOT_READY = "Your peers are not yet ready to load this level";

	private final String ROOM_NAME_DEDUP_DELIMITER = "_";

	// Should support multiple concurrent game rooms, i.e. need multiple
	// concurrent engines
	private Map<String, String> roomNamesToGameNames = new HashMap<>();
	private Map<String, List<Integer>> roomMembers = new HashMap<>();
	private Map<String, Integer> roomNameCollisions = new HashMap<>();
	private Map<Integer, String> clientIdsToUserNames = new HashMap<>();
	private Map<String, Integer> waitingInRoom = new HashMap<>();

	private ObservableList<ServerMessage> messageQueue = FXCollections.observableArrayList();

	public byte[] handleRequestAndSerializeResponse(int clientId, byte[] requestBytes) {
		try {
			if (isAuthoringSpecificRequest(requestBytes)) {
				System.out.println("Detected authoring-specific request");
				return handleSpecificRequestAndSerializeResponse(clientId, requestBytes);
			}
			System.out.println("Handling request");
			ClientMessage clientMessage = ClientMessage.parseFrom(requestBytes);
			System.out.println(clientMessage.toString());
			byte[] pregameResponseBytes = handlePreGameRequestAndSerializeResponse(clientId,
					clientMessage, ServerMessage.newBuilder());
			if (pregameResponseBytes.length > 0) {
				System.out.println("Returning pregame response");
				return pregameResponseBytes;
			}
			System.out.println("Handling specific request");
			return handleSpecificRequestAndSerializeResponse(clientId, requestBytes);
		} catch (InvalidProtocolBufferException | ReflectiveOperationException e) {
			System.out.println("UNABLE TO PARSE INTO CLIENT REQUEST");
			//return handleSpecificRequestAndSerializeResponse(clientId, requestBytes);
			return new byte[] {};
		}
	}

	public void registerNotificationStreamListener(ListChangeListener<Message> listener) {
		System.out.println("Registering notification listener");
		messageQueue.addListener(listener);
	}

	public void disconnectClient(int clientId) {
		clientIdsToUserNames.remove(clientId);
		roomMembers.entrySet().forEach(roomEntry -> {
			if (roomEntry.getValue().contains(clientId)) {
				roomEntry.getValue().remove(new Integer(clientId));
			}
		});
	}

	protected abstract byte[] handleSpecificRequestAndSerializeResponse(int clientId, byte[] requestBytes);

	protected byte[] getAvailableGames(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		Games.Builder gamesBuilder = Games.newBuilder();
		Map<String, String> availableGames = new PlayController().getAvailableGames();
		availableGames.keySet().forEach(gameName -> gamesBuilder
				.addGames(Game.newBuilder().setName(gameName).setDescription(availableGames.get(gameName)).build()));
		return serverMessageBuilder.setAvailableGames(gamesBuilder.build()).build().toByteArray();
	}

	protected byte[] createGameRoom(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		CreateGameRoom gameRoomCreationRequest = clientMessage.getCreateGameRoom();
		GameRoomCreationStatus.Builder gameRoomCreationStatusBuilder = GameRoomCreationStatus.newBuilder();
		// Only allow a given client process to play one game at a time
		if (clientIdsToUserNames.containsKey(clientId)) {
			return serverMessageBuilder
					.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setError(ERROR_CLIENT_ENGAGED).build())
					.build().toByteArray();
		}
		String gameName = gameRoomCreationRequest.getGameName();
		String roomName = generateUniqueRoomName(gameRoomCreationRequest.getRoomName());
		createEngineForRoom(roomName);
		AbstractGameController controllerForGame = getEngineForRoom(roomName);
		// Verify that gameName is valid
		System.out.println("Looking for game " + gameName);
		if (!controllerForGame.getAvailableGames().containsKey(gameName)) {
			/*
			 * System.out.println("GAME NOT AVAILABLE"); return serverMessageBuilder
			 * .setGameRoomCreationStatus( gameRoomCreationStatusBuilder.setError(
			 * GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build()) .build().toByteArray();
			 */
			roomNamesToGameNames.put(roomName, Constants.NEW_GAME_NAME);
			// return
			// serverMessageBuilder.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setRoomId(value))
		} else {
			roomNamesToGameNames.put(roomName, gameName);
		}
		roomMembers.put(roomName, new ArrayList<>());
		clearWaitingRoom(roomName);
		return serverMessageBuilder.setGameRoomCreationStatus(gameRoomCreationStatusBuilder.setRoomId(roomName).build())
				.build().toByteArray();
	}

	protected byte[] joinGameRoom(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		JoinRoom joinRoomRequest = clientMessage.getJoinRoom();
		GameRoomJoinStatus.Builder gameRoomJoinStatusBuilder = GameRoomJoinStatus.newBuilder();
		// Check if client is already in some other game
		if (clientIsInAGameRoom(clientId)) {
			return serverMessageBuilder
					.setGameRoomJoinStatus(
							gameRoomJoinStatusBuilder.setSuccess(false).setError(ERROR_CLIENT_ENGAGED).build())
					.build().toByteArray();
		}
		String roomName = joinRoomRequest.getRoomName();
		if (!roomMembers.containsKey(roomName)) {
			return serverMessageBuilder
					.setGameRoomJoinStatus(
							gameRoomJoinStatusBuilder.setSuccess(false).setError(ERROR_NONEXISTENT_ROOM).build())
					.build().toByteArray();
		}
		String userName = joinRoomRequest.getUserName();
		// Check if username is taken within this room
		if (userNameExistsInGameRoom(userName, roomName)) {
			return serverMessageBuilder.setGameRoomJoinStatus(
					gameRoomJoinStatusBuilder.setSuccess(false).setError(GAME_ROOM_JOIN_ERROR_USERNAME_TAKEN).build())
					.build().toByteArray();
		}
		processUserJoinRoom(clientId, roomName, userName);
		// Push message to other clients
		System.out.println("Adding playerJoin message to messageQueue");
		messageQueue
				.add(ServerMessage.newBuilder()
						.setNotification(Notification.newBuilder()
								.setPlayerJoined(PlayerJoined.newBuilder().setUserName(userName).build()).build())
						.build());
		return serverMessageBuilder.setGameRoomJoinStatus(gameRoomJoinStatusBuilder.setSuccess(true).build()).build()
				.toByteArray();
	}

	protected byte[] exitGameRoom(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		if (clientIsInAGameRoom(clientId)) {
			String exitingUserName = clientIdsToUserNames.get(clientId);
			processUserExitRoom(clientId);
			// Push message to other clients
			System.out.println("Adding playerExit message to messageQueue");
			messageQueue.add(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder()
							.setPlayerExited(PlayerExited.newBuilder().setUserName(exitingUserName).build()).build())
					.build());
		}
		// just ignore if client is not in a game room
		return serverMessageBuilder.build().toByteArray();
	}

	protected byte[] launchGameRoom(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		GameRoomLaunchStatus.Builder gameRoomLaunchStatusBuilder = GameRoomLaunchStatus.newBuilder();
		String roomName = getGameRoomNameOfClient(clientId);
		String gameName = retrieveGameNameFromRoomName(roomName);
		try {
			LevelInitialized levelData = getEngineForRoom(roomName).loadOriginalGameState(gameName, 1);
			serverMessageBuilder
					.setGameRoomLaunchStatus(gameRoomLaunchStatusBuilder.setInitialState(levelData).build());
			// Push message to other clients
			messageQueue.add(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder().setLevelInitialized(levelData).build()).build());
			return serverMessageBuilder.build().toByteArray();
		} catch (IOException e) {
			/*
			 * return serverMessageBuilder .setGameRoomLaunchStatus(
			 * gameRoomLaunchStatusBuilder.setError(
			 * GAME_ROOM_CREATION_ERROR_NONEXISTENT_GAME).build()) .build().toByteArray();
			 */
			// Clean slate
			System.out.println("Clean slate detected, returning empty message");
			messageQueue.add(ServerMessage.newBuilder().setNotification(
					Notification.newBuilder().setLevelInitialized(LevelInitialized.getDefaultInstance()).build())
					.build());
			return serverMessageBuilder
					.setGameRoomLaunchStatus(
							gameRoomLaunchStatusBuilder.setInitialState(LevelInitialized.getDefaultInstance()).build())
					.build().toByteArray();
		}
	}

	protected byte[] getGameRooms(ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.setGameRooms(GameRooms.newBuilder().addAllRoomNames(roomMembers.keySet()).build())
				.build().toByteArray();
	}

	protected byte[] getPlayerNames(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		PlayerNames.Builder playerNamesBuilder = PlayerNames.newBuilder();
		return serverMessageBuilder
				.setPlayerNames(playerNamesBuilder
						.addAllUserNames(getUserNamesInGameRoom(getGameRoomNameOfClient(clientId))).build())
				.build().toByteArray();
	}

	protected boolean joinAndCheckIfWaitingRoomIsFull(String roomName) {
		Map<String, Integer> waitingRoom = getWaitingRoom();
		waitingRoom.put(roomName, waitingRoom.getOrDefault(roomName, 0) + 1);
		return checkIfWaitingRoomIsFull(roomName);
	}

	private byte[] loadLevel(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		LevelInitialized.Builder levelInitializationBuilder = LevelInitialized.newBuilder();
		LoadLevel loadLevelRequest = clientMessage.getLoadLevel();
		String roomName = getGameRoomNameOfClient(clientId);
		int levelToLoad = loadLevelRequest.getLevel();
		if (!checkIfWaitingRoomIsFull(roomName)) {
			// not ready to load
			return serverMessageBuilder
					.setLevelInitialized(levelInitializationBuilder.setError(LOAD_LEVEL_ERROR_NOT_READY).build())
					.build().toByteArray();
		}
		String gameName = retrieveGameNameFromRoomName(roomName);
		try {
			return serverMessageBuilder
					.setLevelInitialized(getEngineForClient(clientId).loadOriginalGameState(gameName, levelToLoad))
					.build().toByteArray();
		} catch (IOException e) {
			return serverMessageBuilder.setLevelInitialized(LevelInitialized.getDefaultInstance()).build()
					.toByteArray();
		}
	}

	private byte[] getLevelElements(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addAllLevelSprites(
						getEngineForClient(clientId).getLevelSprites(clientMessage.getGetLevelElements().getLevel()))
				.build().toByteArray();
	}

	private byte[] getNumberOfLevels(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		System.out.println("Processing num levels request");
		return serverMessageBuilder
				.setNumLevels(NumberOfLevels.newBuilder()
						.setNumLevels(getEngineForClient(clientId).getNumLevelsForGame()).build())
				.build().toByteArray();
	}

	private byte[] placeElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder)
			throws ReflectiveOperationException {
		AbstractGameController controller = getEngineForClient(clientId);
		PlaceElement placeElementRequest = clientMessage.getPlaceElement();
		NewSprite placedElement = controller.placeElement(placeElementRequest.getElementName(),
				new Point2D(placeElementRequest.getXCoord(), placeElementRequest.getYCoord()));
		// Broadcast
		enqueueMessage(ServerMessage.newBuilder()
				.setNotification(Notification.newBuilder().setElementPlaced(placedElement).build()).build());
		return serverMessageBuilder.setElementPlaced(placedElement).build().toByteArray();
	}

	private byte[] moveElement(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		AbstractGameController controller = getEngineForRoom(getGameRoomNameOfClient(clientId));
		MoveElement moveElementRequest = clientMessage.getMoveElement();
		SpriteUpdate updatedSprite = controller.moveElement(moveElementRequest.getElementId(),
				moveElementRequest.getNewXCoord(), moveElementRequest.getNewYCoord());
		// Broadcast
		enqueueMessage(ServerMessage.newBuilder()
				.setNotification(Notification.newBuilder().setElementMoved(updatedSprite).build()).build());
		return serverMessageBuilder.setElementMoved(updatedSprite).build().toByteArray();
	}

	private byte[] deleteElement(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		AbstractGameController controller = getEngineForRoom(getGameRoomNameOfClient(clientId));
		DeleteElement deleteElementRequest = clientMessage.getDeleteElement();
		try {
			SpriteDeletion deletedElement = controller.deleteElement(deleteElementRequest.getElementId());
			enqueueMessage(ServerMessage.newBuilder()
					.setNotification(Notification.newBuilder().setElementDeleted(deletedElement).build()).build());
			return serverMessageBuilder.setElementDeleted(deletedElement).build().toByteArray();
		} catch (IllegalArgumentException e) {
			return serverMessageBuilder.setError(e.getMessage()).build().toByteArray();
		}
	}

	protected void enqueueMessage(ServerMessage message) {
		messageQueue.add(message);
	}

	protected boolean clientIsInAGameRoom(int clientId) {
		return roomMembers.values().stream().filter(clientIds -> clientIds.contains(clientId)).count() > 0;
	}

	protected abstract AbstractGameController getEngineForRoom(String room);

	protected abstract void createEngineForRoom(String room);

	protected abstract AbstractGameController getEngineForClient(int clientId);

	protected String getGameRoomNameOfClient(int clientId) {
		return roomMembers.keySet().stream().filter(roomName -> roomMembers.get(roomName).contains(clientId)).iterator()
				.next();
	}

	protected boolean clientIsFirstMemberOfGameRoom(int clientId) {
		if (!clientIsInAGameRoom(clientId)) {
			return false;
		}
		return roomMembers.get(getGameRoomNameOfClient(clientId)).iterator().next().equals(clientId);
	}

	protected String retrieveGameNameFromRoomName(String roomName) throws IllegalArgumentException {
		if (!roomNamesToGameNames.containsKey(roomName)) {
			throw new IllegalArgumentException(ERROR_NONEXISTENT_ROOM);
		}
		return roomNamesToGameNames.get(roomName);
	}

	protected Map<String, Integer> getWaitingRoom() {
		return waitingInRoom;
	}

	private boolean checkIfWaitingRoomIsFull(String roomName) {
		return getWaitingRoom().get(roomName) < getRoomSize(roomName);
	}

	protected int getRoomSize(String roomName) {
		return roomMembers.get(roomName).size();
	}
	
	private byte[] getInventory(int clientId, ClientMessage clientMessage, ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.setInventory(getEngineForClient(clientId).packageInventory()).build().toByteArray();
	}

	private byte[] getTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addTemplateProperties(getEngineForClient(clientId)
						.packageTemplateProperties(clientMessage.getGetTemplateProperties().getElementName()))
				.build().toByteArray();
	}

	private byte[] getAllTemplateProperties(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder
				.addAllTemplateProperties(getEngineForClient(clientId).packageAllTemplateProperties()).build()
				.toByteArray();
	}

	private byte[] getElementCosts(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllElementCosts(getEngineForClient(clientId).packageAllElementCosts()).build()
				.toByteArray();
	}


	private boolean userNameExistsInGameRoom(String userName, String gameRoomName) {
		return roomMembers.get(gameRoomName).stream().map(clientId -> clientIdsToUserNames.get(clientId))
				.collect(Collectors.toSet()).contains(userName);
	}

	private Set<String> getUserNamesInGameRoom(String gameRoomName) {
		return roomMembers.get(gameRoomName).stream().map(roomMemberId -> clientIdsToUserNames.get(roomMemberId))
				.collect(Collectors.toSet());
	}

	private String generateUniqueRoomName(String roomName) {
		int numCollisions = roomNameCollisions.getOrDefault(roomName, 0);
		roomNameCollisions.put(roomName, numCollisions + 1);
		if (numCollisions > 0) {
			roomName += ROOM_NAME_DEDUP_DELIMITER + Integer.toString(numCollisions);
		}
		return roomName;
	}

	private void clearWaitingRoom(String roomName) {
		waitingInRoom.put(roomName, 0);
	}

	private void processUserJoinRoom(int clientId, String roomName, String userName) {
		roomMembers.get(roomName).add(clientId);
		clientIdsToUserNames.put(clientId, userName);
	}

	private void processUserExitRoom(int clientId) {
		clientIdsToUserNames.remove(clientId);
		roomMembers.get(getGameRoomNameOfClient(clientId)).remove(new Integer(clientId));
	}

	private byte[] handlePreGameRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) throws ReflectiveOperationException {
		if (clientMessage.hasGetAvailableGames()) {
			return getAvailableGames(clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetGameRooms()) {
			return getGameRooms(clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasCreateGameRoom()) {
			return createGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasJoinRoom()) {
			return joinGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		// Following requests need user to be in a game room
		if (!clientIsInAGameRoom(clientId)) {
			return serverMessageBuilder.setError(ERROR_UNAUTHORIZED).build().toByteArray();
		}

		if (clientMessage.hasExitRoom()) {
			return exitGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasLaunchGameRoom()) {
			return launchGameRoom(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetPlayerNames()) {
			return getPlayerNames(clientId, clientMessage, serverMessageBuilder);
		}
		System.out.println("No pre-game request, checking for common game request");
		return handleCommonGameRequestAndSerializeResponse(clientId, clientMessage, serverMessageBuilder);
	}

	private byte[] handleCommonGameRequestAndSerializeResponse(int clientId, ClientMessage clientMessage,
			ServerMessage.Builder serverMessageBuilder) throws ReflectiveOperationException {
		if (clientMessage.hasLoadLevel()) {
			return loadLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetLevelElements()) {
			return getLevelElements(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetNumLevels()) {
			return getNumberOfLevels(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasPlaceElement()) {
			return placeElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasMoveElement()) {
			return moveElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasDeleteElement()) {
			return deleteElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetInventory()) {
			return getInventory(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetTemplateProperties()) {
			return getTemplateProperties(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetAllTemplateProperties()) {
			System.out.println("Returning template properties response");
			return getAllTemplateProperties(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetElementCosts()) {
			return getElementCosts(clientId, clientMessage, serverMessageBuilder);
		}
		return serverMessageBuilder.getDefaultInstanceForType().toByteArray();
	}
	
	private boolean isAuthoringSpecificRequest(byte[] request) {
		try {
			AuthoringClientMessage authClientMessage = AuthoringClientMessage.parseFrom(request);
			return authClientMessage.getForAuthoring();
		} catch (InvalidProtocolBufferException e) {
			return false;
		}
	}

}
