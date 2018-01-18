package networking;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import engine.PlayModelController;
import engine.behavior.movement.LocationProperty;
import networking.protocol.PlayerClient.CheckReadyForNextLevel;
import networking.protocol.PlayerClient.ClientMessage;
import networking.protocol.PlayerClient.PauseGame;
import networking.protocol.PlayerClient.PerformUpdate;
import networking.protocol.PlayerClient.ResumeGame;
import networking.protocol.PlayerClient.UpgradeElement;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.ServerMessage;
import networking.protocol.PlayerServer.StatusUpdate;
import networking.protocol.PlayerServer.Update;

/**
 * Gateway of player in multi-player game to remote back-end data and logic
 * Provides abstraction of a local controller / back-end to the player front-end
 * by providing the same interface
 * 
 * @author radithya
 *
 */
public class MultiPlayerClient extends AbstractClient implements PlayModelController { // Is this weird?

	public MultiPlayerClient() {
		super();
	}

	// Since saving is not allowed, this won't be allowed either
	@Override
	public LevelInitialized loadSavedPlayState(String savePlayStateName) throws UnsupportedOperationException {
		// TODO - Define custom exception in exceptions properties file and pass that
		// string here
		throw new UnsupportedOperationException();
	}

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param fileName
	 *            the name to assign to the save file
	 */
	@Override
	public void saveGameState(String fileName) throws UnsupportedOperationException {
		// TODO - Define custom exception in exceptions properties file and pass that
		// string here
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Update update() {
		System.out.println("Requesting update");
		writeRequestBytes(
				ClientMessage.newBuilder().setPerformUpdate(PerformUpdate.getDefaultInstance()).build().toByteArray());
		System.out.println("Made update request!");
		return handleUpdateResponse(pollFromMessageQueue());
	}

	@Override
	public void pause() {
		writeRequestBytes(
				ClientMessage.newBuilder().setPauseGame(PauseGame.getDefaultInstance()).build().toByteArray());
		handleUpdateResponse(pollFromMessageQueue());
	}

	@Override
	public void resume() {
		writeRequestBytes(
				ClientMessage.newBuilder().setResumeGame(ResumeGame.getDefaultInstance()).build().toByteArray());
		handleUpdateResponse(pollFromMessageQueue());
	}

	@Override
	public boolean isLost() {
		return getLatestStatusUpdate().getIsLost();
	}

	@Override
	public boolean isLevelCleared() {
		return getLatestStatusUpdate().getLevelCleared();
	}

	@Override
	public boolean isReadyForNextLevel() {
		writeRequestBytes(ClientMessage.newBuilder()
				.setCheckReadyForNextLevel(CheckReadyForNextLevel.getDefaultInstance()).build().toByteArray());
		return handleCheckReadyResponse(pollFromMessageQueue());
	}

	@Override
	public boolean isWon() {
		return getLatestStatusUpdate().getIsWon();
	}

	@Override
	public int getCurrentLevel() {
		return getLatestStatusUpdate().getCurrentLevel();
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		Update latestUpdate = getLatestUpdate();
		ResourceUpdate resourceUpdate = latestUpdate.hasResourceUpdates() ? latestUpdate.getResourceUpdates()
				: ResourceUpdate.getDefaultInstance();
		Map<String, Double> resourcesMap = new HashMap<>();
		resourceUpdate.getResourcesList()
				.forEach(resource -> resourcesMap.put(resource.getName(), resource.getAmount()));
		return resourcesMap;
	}

	@Override
	public double getElementPointValue(int elementId) {
		return 0;
	}

	@Override
	public LocationProperty getElementLocationProperty(int elementId) {
		// no time for this
		return null;
	}

	@Override
	public void triggerFire(int elementId) {
		// do nothing, no time
	}

	@Override
	public void upgradeElement(int elementId) throws IllegalArgumentException {
		writeRequestBytes(ClientMessage.newBuilder()
				.setUpgradeElement(UpgradeElement.newBuilder().setSpriteId(elementId).build()).build().toByteArray());
		pollFromMessageQueue();
	}
	
	

	@Override
	protected int getPort() {
		return Constants.MULTIPLAYER_SERVER_PORT;
	}

	private Update handleUpdateResponse(ServerMessage serverMessage) {
		return getUpdate(serverMessage);
	}

	private boolean handleCheckReadyResponse(ServerMessage serverMessage) {
		if (serverMessage.hasReadyForNextLevel()) {
			return serverMessage.getReadyForNextLevel().getIsReady();
		}
		return false;
	}

	private Update getUpdate(ServerMessage serverMessage) {
		if (serverMessage.hasUpdate()) {
			return serverMessage.getUpdate();
		}
		return Update.getDefaultInstance();
	}

	private StatusUpdate getLatestStatusUpdate() {
		Update latestUpdate = getLatestUpdate();
		return latestUpdate.hasStatusUpdates() ? latestUpdate.getStatusUpdates() : StatusUpdate.getDefaultInstance();
	}

	// Test client-server integration
	public static void main(String[] args) {
		MultiPlayerClient testClient = new MultiPlayerClient();
		testClient.launchNotificationListener();
		System.out.println("Getting available games");
		Map<String, String> availableGames = testClient.getAvailableGames();
		for (String gameName : availableGames.keySet()) {
			System.out.println("Game name: " + gameName);
		}
		System.out.println("Creating game room");
		String gameRoom = testClient.createGameRoom("savetest.voog", "adi_game");
		System.out.println("Joined " + gameRoom);
		testClient.joinGameRoom(gameRoom, "adi");
		testClient.launchGameRoom();
		System.out.println("Player names: ");
		Set<String> playerNames = testClient.getPlayerNames();
		for (String playerName : playerNames) {
			System.out.println("Player: " + playerName);
		}
		System.out.println("Current level: " + testClient.getCurrentLevel());
		Set<String> inventory = testClient.getInventory();
		for (String item : inventory) {
			System.out.println("Item: " + item);
		}
		testClient.exitGameRoom();
	}

}