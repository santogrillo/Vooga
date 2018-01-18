package networking;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import engine.AuthoringModelController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import networking.protocol.AuthorClient.AddElementToInventory;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorClient.CreateWaveProperties;
import networking.protocol.AuthorClient.DefineElement;
import networking.protocol.AuthorClient.DefineElementUpgrade;
import networking.protocol.AuthorClient.DeleteElementDefinition;
import networking.protocol.AuthorClient.DeleteLevel;
import networking.protocol.AuthorClient.EditWaveProperties;
import networking.protocol.AuthorClient.ExportGame;
import networking.protocol.AuthorClient.GetAllDefinedElementUpgrades;
import networking.protocol.AuthorClient.GetAuxiliaryElementConfigurationOptions;
import networking.protocol.AuthorClient.GetCurrentDefeatConditions;
import networking.protocol.AuthorClient.GetCurrentLevel;
import networking.protocol.AuthorClient.GetCurrentVictoryConditions;
import networking.protocol.AuthorClient.GetElementBaseConfigurationOptions;
import networking.protocol.AuthorClient.GetPossibleDefeatConditions;
import networking.protocol.AuthorClient.GetPossibleVictoryConditions;
import networking.protocol.AuthorClient.GetResourceEndowments;
import networking.protocol.AuthorClient.GetWaveProperties;
import networking.protocol.AuthorClient.SetLevel;
import networking.protocol.AuthorClient.SetStatusProperty;
import networking.protocol.AuthorClient.SetUnitCost;
import networking.protocol.AuthorClient.SetVictoryCondition;
import networking.protocol.AuthorClient.UpdateElementDefinition;
import networking.protocol.AuthorClient.UpdateElementProperties;
import networking.protocol.AuthorClient.Property;
import networking.protocol.AuthorClient.ResourceEndowment;
import networking.protocol.AuthorClient.SetDefeatCondition;
import networking.protocol.AuthorClient.SetGameDescription;
import networking.protocol.AuthorClient.SetGameName;
import networking.protocol.AuthorServer.AuthoringNotification;
import networking.protocol.AuthorServer.AuthoringServerMessage;
import networking.protocol.PlayerServer.LevelInitialized;
import util.protocol.ServerMessageUtils;

public class CollaborativeAuthoringClient extends AbstractClient implements AuthoringModelController {

	private ObservableList<AuthoringNotification> notificationQueue = FXCollections.observableArrayList();
	private ServerMessageUtils serverMessageUtils = new ServerMessageUtils();
	private Queue<AuthoringServerMessage> messageQueue;

	public CollaborativeAuthoringClient() {
		super();
		messageQueue = new ArrayDeque<>();
	}

	@Override
	public void registerNotificationListener(ListChangeListener<? super Message> listener) {
		super.registerNotificationListener(listener);
		notificationQueue.addListener(listener);
		System.out.println("Registered notification listener");
	}

	// TODO
	@Override
	public void saveGameState(String fileNameToSaveTo) {
		// TODO Auto-generated method stub

	}

	@Override
	public String exportGame() throws IOException {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setExportGame(ExportGame.getDefaultInstance()).build()
				.toByteArray());
		// TODO - Corresponding server message
		return "";
	}

	@Override
	public void setLevel(int level) {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setSetLevel(SetLevel.newBuilder().setLevel(level))
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void deleteLevel(int level) throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setDeleteLevel(DeleteLevel.newBuilder().setLevel(level))
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public int getCurrentLevel() {
		writeRequestBytes(AuthoringClientMessage.newBuilder().setGetCurrentLevel(GetCurrentLevel.getDefaultInstance())
				.setForAuthoring(true).build().toByteArray());
		return handleCurrentLevelResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Map<String, List<String>> getElementBaseConfigurationOptions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetElementBaseConfig(GetElementBaseConfigurationOptions.getDefaultInstance()).setForAuthoring(true)
				.build().toByteArray());
		return handleElementBaseConfigurationOptionsResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetAuxiliaryElementConfig(GetAuxiliaryElementConfigurationOptions.newBuilder()
						.addAllBaseConfigurationChoices(baseConfigurationChoices.entrySet().stream()
								.map(entry -> Property.newBuilder().setName(entry.getKey()).setValue(entry.getValue())
										.build())
								.collect(Collectors.toList())))
				.setForAuthoring(true).build().toByteArray());
		return handleAuxiliaryElementConfigurationOptions(pollFromAuthoringMessageQueue());
	}

	@Override
	public void defineElement(String elementName, Map<String, Object> properties) throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setDefineElement(serverMessageUtils.packageDefinedElement(elementName, properties))
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void defineElementUpgrade(String elementName, int upgradeLevel, Map<String, Object> upgradeProperties)
			throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setDefineElementUpgrade(DefineElementUpgrade.newBuilder().setElementName(elementName)
						.setUpgradeLevel(upgradeLevel)
						.addAllProperties(serverMessageUtils.getPropertiesFromObjectMap(upgradeProperties)).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void updateElementDefinition(String elementName, Map<String, Object> propertiesToUpdate, boolean retroactive)
			throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setUpdateElementDefinition(UpdateElementDefinition.newBuilder().setElementName(elementName)
						.addAllProperties(serverMessageUtils.getPropertiesFromObjectMap(propertiesToUpdate))
						.setRetroactive(retroactive).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void deleteElementDefinition(String elementName) throws IllegalArgumentException {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setDeleteElementDefinition(DeleteElementDefinition.newBuilder().setElementName(elementName).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public DefineElement addElementToInventory(String elementName) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setAddElementToInventory(AddElementToInventory.newBuilder().setElementName(elementName).build())
				.setForAuthoring(true).build().toByteArray());
		return handleDefineElementResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public void updateElementProperties(int elementId, Map<String, Object> propertiesToUpdate) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setUpdateElementProperties(UpdateElementProperties.newBuilder().setElementId(elementId)
						.addAllProperties(serverMessageUtils.getPropertiesFromObjectMap(propertiesToUpdate)).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public Map<String, List<Map<String, Object>>> getAllDefinedElementUpgrades() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetAllDefinedElementUpgrades(GetAllDefinedElementUpgrades.getDefaultInstance()).setForAuthoring(true).build()
				.toByteArray());
		return handleAllDefinedElementUpgradesResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetResourceEndowments(GetResourceEndowments.getDefaultInstance()).setForAuthoring(true).build().toByteArray());
		return handleResourceEndowmentsResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public void setGameName(String gameName) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetGameName(SetGameName.newBuilder().setGameName(gameName).build()).setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void setGameDescription(String gameDescription) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetGameDescription(SetGameDescription.newBuilder().setGameDescription(gameDescription).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void setVictoryCondition(String conditionIdentifier) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetVictoryCondition(
						SetVictoryCondition.newBuilder().setConditionIdentifier(conditionIdentifier).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void setDefeatCondition(String conditionIdentifier) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetDefeatCondition(
						SetDefeatCondition.newBuilder().setConditionIdentifier(conditionIdentifier).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void setStatusProperty(String property, Double value) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetStatusProperty(
						SetStatusProperty.newBuilder().setPropertyName(property).setPropertyValue(value).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void setResourceEndowments(Map<String, Double> resourceEndowments) {
		resourceEndowments.entrySet().forEach(entry -> setResourceEndowment(entry.getKey(), entry.getValue()));
	}

	@Override
	public void setResourceEndowment(String resourceName, double newResourceEndowment) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.addSetResourceEndowments(
						ResourceEndowment.newBuilder().setName(resourceName).setAmount(newResourceEndowment).build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public void setUnitCost(String elementName, Map<String, Double> unitCosts) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setSetUnitCost(SetUnitCost.newBuilder().setElementName(elementName)
						.addAllElementCosts(unitCosts.entrySet().stream()
								.map(entry -> ResourceEndowment.newBuilder().setName(entry.getKey())
										.setAmount(entry.getValue()).build())
								.collect(Collectors.toList()))
						.build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public int createWaveProperties(Map<String, Object> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setCreateWaveProperties(
						makeCreateWavePropertiesMessage(waveProperties, elementNamesToSpawn, spawningPoint))
				.setForAuthoring(true).build().toByteArray());
		return handleCreateWavePropertiesResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public void editWaveProperties(int waveNum, Map<String, Object> updatedProperties,
			Collection<String> newElementNamesToSpawn, Point2D newSpawningPoint) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setEditWaveProperties(EditWaveProperties.newBuilder().setWaveNum(waveNum).setEditProperties(
						makeCreateWavePropertiesMessage(updatedProperties, newElementNamesToSpawn, newSpawningPoint))
						.build())
				.setForAuthoring(true).build().toByteArray());
	}

	@Override
	public Map<String, Object> getWaveProperties(int waveNum) {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetWaveProperties(GetWaveProperties.newBuilder().setWaveNum(waveNum).build()).setForAuthoring(true).build()
				.toByteArray());
		return handleWavePropertiesResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Collection<String> getPossibleVictoryConditions() {
		System.out.println("Getting possible victory conditions");
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetPossibleVictoryConditions(GetPossibleVictoryConditions.getDefaultInstance()).setForAuthoring(true).build()
				.toByteArray());
		return handleConditionsResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Collection<String> getPossibleDefeatConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetPossibleDefeatConditions(GetPossibleDefeatConditions.getDefaultInstance()).setForAuthoring(true).build()
				.toByteArray());
		return handleConditionsResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentVictoryConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetCurrentVictoryConditions(GetCurrentVictoryConditions.getDefaultInstance()).setForAuthoring(true).build()
				.toByteArray());
		return handleCurrentVictoryConditionsResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public Map<String, Collection<Integer>> getCurrentDefeatConditions() {
		writeRequestBytes(AuthoringClientMessage.newBuilder()
				.setGetCurrentDefeatConditions(GetCurrentDefeatConditions.getDefaultInstance()).setForAuthoring(true).build().toByteArray());
		return handleCurrentDefeatConditionsResponse(pollFromAuthoringMessageQueue());
	}

	@Override
	public String getGameName() {
		// todo (lol)
		return "TODO LOL";
	}

	@Override
	public void setLevelHealth(int health) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLevelPointQuota(int points) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLevelTimeLimit(int timeLimit) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getPort() {
		return Constants.COLLABORATIVE_AUTHORING_SERVER_PORT;
	}

	@Override
	protected boolean appendMessageToAppropriateQueue(byte[] requestBytes) {
		if (!super.appendMessageToAppropriateQueue(requestBytes)) {
			try {
				System.out.println("Trying to form and append authoring message");
				AuthoringServerMessage authoringServerMessage = AuthoringServerMessage.parseFrom(requestBytes);
				if (authoringServerMessage.hasNotification()) {
					System.out.println("Appending authoring notification");
					System.out.println(authoringServerMessage.getNotification().toString());
					appendNotificationToQueue(authoringServerMessage.getNotification(), notificationQueue);
				} else {
					System.out.println("Appending normal authoring message: ");
					System.out.println(authoringServerMessage.toString());
					appendMessageToQueue(authoringServerMessage, messageQueue);
				}
				return true;
			} catch (InvalidProtocolBufferException e) {
				System.out.println("Could not append message to EITHER queue!");
				return false;
			}
		}
		System.out.println("Message was appended to common queue");
		return true;
	}

	private AuthoringServerMessage pollFromAuthoringMessageQueue() {
		System.out.println("About to poll from authoring message queue");
		AuthoringServerMessage polledMessage = pollFromCustomMessageQueue(messageQueue);
		return polledMessage == null ? AuthoringServerMessage.getDefaultInstance() : polledMessage;
	}

	private Map<String, List<String>> handleElementBaseConfigurationOptionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, List<String>> configOptions = new HashMap<>();
		System.out.println("HANDLING BASE CONFIG OPTIONS RESPONSE");
		System.out.println(authoringServerMessage.toString());
		authoringServerMessage.getElementBaseConfigurationOptionsList()
				.forEach(config -> configOptions.put(config.getConfigKey(), config.getConfigOptionsList()));
		return configOptions;
	}

	private Map<String, Class> handleAuxiliaryElementConfigurationOptions(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, Class> auxiliaryElementConfigOptions = new HashMap<>();
		authoringServerMessage.getAuxiliaryElementConfigurationOptionsList().forEach(config -> {
			try {
				auxiliaryElementConfigOptions.put(config.getConfigName(), Class.forName(config.getConfigClassName()));
			} catch (ClassNotFoundException e) {
				// TODO - Custom Exception
			}
		});
		return auxiliaryElementConfigOptions;
	}

	private Map<String, List<Map<String, Object>>> handleAllDefinedElementUpgradesResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, List<Map<String, Object>>> allElementUpgradesMap = new HashMap<>();
		authoringServerMessage.getAllDefinedElementUpgradesList().forEach(upgrade -> allElementUpgradesMap
				.put(upgrade.getElementName(), upgrade.getElementUpgradesList().stream().map(elementUpgrades -> {
					Map<String, String> elementUpgradesMap = new HashMap<>();
					elementUpgrades.getItemsList().stream()
							.forEach(item -> elementUpgradesMap.put(item.getName(), item.getValue()));
					return getSerializationUtils().deserializeElementTemplate(elementUpgradesMap);
				}).collect(Collectors.toList())));
		return allElementUpgradesMap;
	}

	private Map<String, Double> handleResourceEndowmentsResponse(AuthoringServerMessage authoringServerMessage) {
		Map<String, Double> resourceEndowmentsMap = new HashMap<>();
		authoringServerMessage.getResourceEndowmentsList().stream()
				.forEach(resource -> resourceEndowmentsMap.put(resource.getName(), resource.getValue()));
		return resourceEndowmentsMap;
	}

	private int handleCreateWavePropertiesResponse(AuthoringServerMessage authoringServerMessage) {
		if (authoringServerMessage.hasCreatedWaveNumber()) {
			return authoringServerMessage.getCreatedWaveNumber();
		}
		return 0;
	}

	private Map<String, Object> handleWavePropertiesResponse(AuthoringServerMessage authoringServerMessage) {
		Map<String, String> stringifiedWaveProperties = new HashMap<>();
		authoringServerMessage.getWavePropertiesList()
				.forEach(property -> stringifiedWaveProperties.put(property.getName(), property.getValue()));
		return getSerializationUtils().deserializeElementTemplate(stringifiedWaveProperties);
	}

	private Collection<String> handleConditionsResponse(AuthoringServerMessage authoringServerMessage) {
		System.out.println("Conditions response: " + authoringServerMessage.toString());
		return authoringServerMessage.getPossibleVictoryConditionsList();
	}

	private Map<String, Collection<Integer>> handleCurrentVictoryConditionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, Collection<Integer>> victoryConditionsMap = new HashMap<>();
		authoringServerMessage.getCurrentVictoryConditionsList().forEach(condition -> victoryConditionsMap
				.put(condition.getConditionName(), condition.getLevelsUsingConditionList()));
		return victoryConditionsMap;
	}

	private Map<String, Collection<Integer>> handleCurrentDefeatConditionsResponse(
			AuthoringServerMessage authoringServerMessage) {
		Map<String, Collection<Integer>> defeatConditionsMap = new HashMap<>();
		authoringServerMessage.getCurrentDefeatConditionsList().forEach(condition -> defeatConditionsMap
				.put(condition.getConditionName(), condition.getLevelsUsingConditionList()));
		return defeatConditionsMap;
	}

	private DefineElement handleDefineElementResponse(AuthoringServerMessage authoringServerMessage) {
		return authoringServerMessage.hasElementAddedToInventory() ? authoringServerMessage.getElementAddedToInventory()
				: DefineElement.getDefaultInstance();
	}

	private int handleCurrentLevelResponse(AuthoringServerMessage authoringServerMessage) {
		return authoringServerMessage.getCurrentLevel();
	}

	private CreateWaveProperties makeCreateWavePropertiesMessage(Map<String, Object> waveProperties,
			Collection<String> elementNamesToSpawn, Point2D spawningPoint) {
		Map<String, String> stringifiedWaveProperties = getSerializationUtils()
				.serializeElementTemplate(waveProperties);
		return CreateWaveProperties.newBuilder()
				.addAllWaveProperties(stringifiedWaveProperties.entrySet().stream()
						.map(entry -> Property.newBuilder().setName(entry.getKey()).setValue(entry.getValue()).build())
						.collect(Collectors.toList()))
				.build();
	}

	public static void main(String[] args) {
		CollaborativeAuthoringClient testClient = new CollaborativeAuthoringClient();
		testClient.launchNotificationListener();
		System.out.println("About to query available games for collaborative authoring");
		Map<String, String> games = testClient.getAvailableGames();
		System.out.println("Games:");
		String gameToJoin = "";
		for (String game : games.keySet()) {
			System.out.println(game);
			gameToJoin = game;
		}
		System.out.println("Creating game room : " + gameToJoin);
		String roomCreated = testClient.createGameRoom(gameToJoin, "Adi_Game");
		System.out.println("Created game room " + roomCreated);
		testClient.joinGameRoom(roomCreated, "Adi");
		System.out.println("Joined game room: " + roomCreated);
		LevelInitialized levelData = testClient.launchGameRoom();
		System.out.println("Level data: " + levelData.toString());
	}

	@Override
	public int getNumWavesForLevel(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

}
