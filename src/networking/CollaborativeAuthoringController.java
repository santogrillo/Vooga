package networking;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorClient.DefineElement;
import networking.protocol.AuthorServer.AuthoringNotification;
import networking.protocol.AuthorServer.AuthoringServerMessage;
import networking.protocol.PlayerServer.ServerMessage;
import util.io.SerializationUtils;

public class CollaborativeAuthoringController extends AbstractServerController {

	private SerializationUtils serializationUtils = new SerializationUtils();

	private Map<String, AuthoringController> roomsToEngines = new HashMap<>();

	private ObservableList<AuthoringServerMessage> messageQueue = FXCollections.observableArrayList();

	public byte[] handleSpecificRequestAndSerializeResponse(int clientId, byte[] requestBytes) {
		try {
			AuthoringClientMessage clientMessage = AuthoringClientMessage.parseFrom(requestBytes);
			AuthoringServerMessage.Builder serverMessageBuilder = AuthoringServerMessage.newBuilder();
			serverMessageBuilder.setForAuthoring(true);
			System.out.println("Authoring-specific request: " + clientMessage.toString());
			byte[] queryResponses = handleQueries(clientId, clientMessage, serverMessageBuilder);
			if (queryResponses.length > 0) {
				return queryResponses;
			}
			byte[] mutationResponses = handleMutations(clientId, clientMessage, serverMessageBuilder);
			if (mutationResponses.length > 0) {
				return mutationResponses;
			}
			return serverMessageBuilder.build().toByteArray();
		} catch (InvalidProtocolBufferException e) {
			System.out.println("Invalid protobuf!");
		}
		//return AuthoringServerMessage.newBuilder().setForAuthoring(true).build().toByteArray();
		return new byte[] {};
	}

	@Override
	public void registerNotificationStreamListener(ListChangeListener<Message> listener) {
		super.registerNotificationStreamListener(listener);
		messageQueue.addListener(listener);
	}

	@Override
	protected AuthoringController getEngineForRoom(String room) {
		return roomsToEngines.get(room);
	}

	@Override
	protected void createEngineForRoom(String room) {
		roomsToEngines.put(room, new AuthoringController());
	}

	private byte[] handleQueries(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasGetElementBaseConfig()) {
			return handleGetElementBaseConfigurationOptions(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetAuxiliaryElementConfig()) {
			return handleGetAuxiliaryElementConfigurationOptions(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetPossibleVictoryConditions()) {
			return handleGetPossibleVictoryConditions(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasGetPossibleDefeatConditions()) {
			return handleGetPossibleDefeatConditions(clientId, clientMessage, serverMessageBuilder);
		}
		// TODO - Other queries
		return new byte[] {};
	}

	private byte[] handleMutations(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		if (clientMessage.hasSetLevel()) {
			return handleSetLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasDeleteLevel()) {
			return handleDeleteLevel(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasDefineElement()) {
			return handleDefineElement(clientId, clientMessage, serverMessageBuilder);
		}
		if (clientMessage.hasAddElementToInventory()) {
			return handleElementAddedToInventory(clientId, clientMessage, serverMessageBuilder);
		}
		// TODO - Other queries
		return new byte[] {};
	}

	// TODO - Handle 'Export game' request

	private byte[] handleSetLevel(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		int levelCreated = clientMessage.getSetLevel().getLevel();
		getEngineForClient(clientId).setLevel(levelCreated);
		// Push notification
		messageQueue.add(AuthoringServerMessage.newBuilder()
				.setNotification(AuthoringNotification.newBuilder().setLevelCreated(levelCreated).build()).build());
		return new byte[] {}; // No response expected
	}

	private byte[] handleDeleteLevel(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		int levelDeleted = clientMessage.getDeleteLevel().getLevel();
		getEngineForClient(clientId).deleteLevel(levelDeleted);
		// Push notification
		messageQueue.add(AuthoringServerMessage.newBuilder()
				.setNotification(AuthoringNotification.newBuilder().setLevelDeleted(levelDeleted).build()).build());
		return new byte[] {};
	}

	private byte[] handleGetElementBaseConfigurationOptions(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		System.out.println("Handling get element base config");
		return serverMessageBuilder.addAllElementBaseConfigurationOptions(
				getEngineForClient(clientId).packageElementBaseConfigurationOptions()).build().toByteArray();
	}

	private byte[] handleGetAuxiliaryElementConfigurationOptions(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		Map<String, String> baseConfigOptions = new HashMap<>();
		clientMessage.getGetAuxiliaryElementConfig().getBaseConfigurationChoicesList().stream()
				.forEach(property -> baseConfigOptions.put(property.getName(), property.getValue()));
		return serverMessageBuilder
				.addAllAuxiliaryElementConfigurationOptions(
						getEngineForClient(clientId).packageAuxiliaryElementConfigurationOptions(baseConfigOptions))
				.build().toByteArray();
	}

	private byte[] handleDefineElement(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		Map<String, String> elementProperties = new HashMap<>();
		DefineElement defineElementRequest = clientMessage.getDefineElement();
		String elementName = defineElementRequest.getElementName();
		defineElementRequest.getPropertiesList()
				.forEach(property -> elementProperties.put(property.getName(), property.getValue()));
		getEngineForClient(clientId).defineElement(elementName,
				serializationUtils.deserializeElementTemplate(elementProperties));
		return new byte[] {};
	}

	private byte[] handleGetPossibleVictoryConditions(int clientId, AuthoringClientMessage clientMessage, AuthoringServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllPossibleVictoryConditions(getEngineForClient(clientId).getPossibleVictoryConditions()).build().toByteArray();
	}
	
	private byte[] handleGetPossibleDefeatConditions(int clientId, AuthoringClientMessage clientMessage, AuthoringServerMessage.Builder serverMessageBuilder) {
		return serverMessageBuilder.addAllPossibleDefeatConditions(getEngineForClient(clientId).getPossibleDefeatConditions()).build().toByteArray();
	}
	

	// TODO - Define Element Upgrade

	// TODO - Update Element Definition

	// TODO - Delete Element Definition

	private byte[] handleElementAddedToInventory(int clientId, AuthoringClientMessage clientMessage,
			AuthoringServerMessage.Builder serverMessageBuilder) {
		DefineElement definedElement = getEngineForClient(clientId)
				.addElementToInventory(clientMessage.getAddElementToInventory().getElementName());
		// Push notification
		messageQueue.add(AuthoringServerMessage.newBuilder()
				.setNotification(AuthoringNotification.newBuilder().setElementAddedToInventory(definedElement).build())
				.build());
		return serverMessageBuilder.setElementAddedToInventory(definedElement).build().toByteArray();
	}

	// TODO - Other methods

	@Override
	protected AuthoringController getEngineForClient(int clientId) {
		return getEngineForRoom(getGameRoomNameOfClient(clientId));
	}

}
