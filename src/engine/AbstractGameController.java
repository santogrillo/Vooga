package engine;

import engine.authoring_engine.AuthoringController;
import engine.game_elements.GameElement;
import engine.game_elements.GameElementFactory;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.ElementCost;
import networking.protocol.PlayerServer.Inventory;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.TemplateProperties;
import engine.game_elements.GameElementUpgrader;
import util.GameConditionsReader;
import util.io.SerializationUtils;
import util.io.GameElementIoHandler;
import util.protocol.ServerMessageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Encapsulates the shared fields and behavior between authoring and playing
 * controllers.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public abstract class AbstractGameController implements AbstractGameModelController {

	protected static final int DEFAULT_MAX_LEVELS = 1;
	protected static final String VICTORY = "victory";
	protected static final String DEFEAT = "defeat";

	private final int ASSUMED_PLAYER_ID = -1;
	
	private final int DEFAULT_POINTS_QUOTA = 100;

	private GameElementIoHandler gameElementIoHandler;
	private SpriteQueryHandler spriteQueryHandler;

	private String gameName;
	private IOController ioController;
	private GameConditionsReader gameConditionsReader;
	private ServerMessageUtils serverMessageUtils;

	private List<Map<String, Double>> levelStatuses = new ArrayList<>();
	private List<List<GameElement>> levelSpritesCache = new ArrayList<>();
	private List<Map<String, String>> levelConditions = new ArrayList<>();
	private List<String> levelDescriptions = new ArrayList<>();
	private List<Bank> levelBanks = new ArrayList<>();
	private List<Set<String>> levelInventories = new ArrayList<>();
	private List<List<GameElement>> levelWaves = new ArrayList<>();
	private List<Integer> levelHealths = new ArrayList<>();
	private List<Integer> levelPointQuotas = new ArrayList<>();
	private List<Integer> levelTimeLimits = new ArrayList<>();

	private List<Map<String, Point2D>> levelWaveTemplates = new ArrayList<>();

	// TODO - move these into own object? Or have them in the sprite factory?
	private AtomicInteger spriteIdCounter;
	private Map<Integer, GameElement> spriteIdMap;

	protected Map<Integer, String> audioMap;

	private GameElementFactory gameElementFactory;
	private GameElementUpgrader gameElementUpgrader;

	// this should be from a properties file? or handled in some better way?
	private final String DEFAULT_GAME_NAME = "untitled";

	private int currentLevel;
	private SerializationUtils serializationUtils;

	public AbstractGameController() {
		this.serializationUtils = new SerializationUtils();
		ioController = new IOController(serializationUtils);
		gameConditionsReader = new GameConditionsReader();
		serverMessageUtils = new ServerMessageUtils();
		initialize();
		gameName = DEFAULT_GAME_NAME;
		spriteIdCounter = new AtomicInteger();
		spriteIdMap = new HashMap<>();
		gameElementFactory = new GameElementFactory();
		gameElementUpgrader = new GameElementUpgrader(gameElementFactory);
		gameElementIoHandler = new GameElementIoHandler(serializationUtils);
		spriteQueryHandler = new SpriteQueryHandler();
		currentLevel = 1;
	}

	/**
	 * Save the current state of the current level a game being played or authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	@Override
	public void saveGameState(String saveName) {
		// Note : saveName overrides previously set gameName if different - need to
		// handle this?
		// Serialize separately for every level
		Map<Integer, String> serializedLevelsData = new HashMap<>();
		for (int level = 1; level < getLevelStatuses().size(); level++) {
			serializedLevelsData.put(level,
					getIoController().getLevelSerialization(level, getLevelDescriptions().get(level),
							getLevelConditions().get(level), getLevelBanks().get(level), getLevelStatuses().get(level),
							levelSpritesCache.get(level), levelInventories.get(level), levelHealths.get(level),
							levelPointQuotas.get(level), levelTimeLimits.get(level)));
		}
		// Serialize map of level to per-level serialized data
		getIoController().saveGameStateForMultipleLevels(saveName, serializedLevelsData, isAuthoring());
		gameElementIoHandler.exportElementTemplates(saveName, gameElementFactory.getAllDefinedTemplateProperties());
		gameElementIoHandler.exportElementUpgrades(saveName, gameElementUpgrader.getSpriteUpgradesForEachTemplate());
		gameElementIoHandler.exportWaves(gameName, levelWaveTemplates);
	}

	/**
	 * Load the detailed state of the original authored game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws IOException
	 *             if the save name does not refer to existing files
	 */
	@Override
	public LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException {
		Collection<GameElement> oldGameElements = getLevelSprites().get(getCurrentLevel());
		//for (int levelToLoad = currentLevel; levelToLoad <= level; levelToLoad++) {
			loadLevelData(saveName, level, true);
		//}
		setGameName(saveName);
		setLevel(level);
		gameElementFactory.loadSpriteTemplates(gameElementIoHandler.loadElementTemplates(gameName));
		gameElementUpgrader.loadSpriteUpgrades(gameElementIoHandler.loadElementUpgrades(gameName));
		levelWaveTemplates = gameElementIoHandler.loadWaves(saveName);
		buildWaves();
		return packageStateChange(oldGameElements);
	}

	private void buildWaves() throws IOException {
		// ordering should be correct because of loading process
		for (int i = 0; i < levelWaveTemplates.size(); i++) {
			Map<String, Point2D> wavesInLevel = levelWaveTemplates.get(i);
			List<GameElement> waves = new ArrayList<>();
			List<String> sortedWaveNames = new ArrayList<>(wavesInLevel.keySet());
			Collections.sort(sortedWaveNames);
			for (String waveName : sortedWaveNames) {
				try {
					System.out.println(waveName + " " + sortedWaveNames + " " + wavesInLevel);
					waves.add(generatePlacedElement(waveName, wavesInLevel.get(waveName)));
				} catch (ReflectiveOperationException e) {
					throw new IOException(e);
				}
			}
			levelWaves.add(translateToOneBasedIndexing(i), waves);
		}
		System.out.println("LEVELWAVETEMPLATES:"+levelWaveTemplates);
	}

	public Inventory packageInventory() {
		return getServerMessageUtils().packageInventory(getInventory());
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	@Override
	public int getNumLevelsForGame(String gameName, boolean forOriginalGame) {
		return getNumLevelsForGame();
	}

	@Override
	public int getNumLevelsForGame() {
		System.out.println("Getting number of levels");
		return getLevelSprites().size() - 1;
	}

	@Override
	public Map<String, Object> getTemplateProperties(String elementName) throws IllegalArgumentException {
		return getGameElementFactory().getTemplateProperties(elementName);
	}

	@Override
	public Map<String, Map<String, Object>> getAllDefinedTemplateProperties() {
		return getGameElementFactory().getAllDefinedTemplateProperties();
	}

	@Override
	public NewSprite placeElement(String elementTemplateName, Point2D startCoordinates)
			 {
		try {
			
			GameElement gameElement = generatePlacedElement(elementTemplateName, startCoordinates);
			gameElementUpgrader.registerNewSprite(elementTemplateName, gameElement);
			int spriteId = cacheAndCreateIdentifier(elementTemplateName, gameElement);
			return serverMessageUtils.packageNewSprite(gameElement, spriteId);			
		} catch (ReflectiveOperationException e) {
			return NewSprite.getDefaultInstance();
		}
	}

	@Override
	public SpriteUpdate moveElement(int elementId, double xCoordinate, double yCoordinate)
			throws IllegalArgumentException {
		GameElement gameElement = getElement(elementId);
		gameElement.setX(xCoordinate);
		gameElement.setY(yCoordinate);
		return getServerMessageUtils().packageUpdatedSprite(gameElement, elementId);
	}

    @Override
    public SpriteDeletion deleteElement(int elementId) throws IllegalArgumentException {
        GameElement removedGameElement = getSpriteIdMap().remove(elementId);
        if (removedGameElement == null) {
        		throw new IllegalArgumentException();
        }
        getLevelSprites().get(getCurrentLevel()).remove(removedGameElement);
		processElementSale(removedGameElement);
        return getServerMessageUtils().packageDeletedSprite(removedGameElement, elementId);
    }

	private void processElementSale(GameElement removedGameElement) {
		String removedElementName = removedGameElement.getTemplateName();
		getLevelBanks().get(getCurrentLevel())
                .gainResourcesFromElement(removedElementName);
	}

	@Override
	public int getCurrentLevel() {
		return currentLevel;
	}

	@Override
	public Set<String> getInventory() {
		return getLevelInventories().get(getCurrentLevel());
	}

	/**
	 * Get resources left for current level
	 * 
	 * @deprecated
	 * @return map of resource name to quantity left
	 */
	@Override
	public Map<String, Double> getStatus() {
		return getLevelStatuses().get(getCurrentLevel());
	}

	@Override
	public Map<String, Double> getResourceEndowments() {
		return getLevelBanks().get(getCurrentLevel()).getResourceEndowments();
	}

	@Override
	public Map<String, Map<String, Double>> getElementCosts() {
		return getLevelBanks().get(getCurrentLevel()).getUnitCosts();
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	@Override
	public Map<String, String> getAvailableGames() throws IllegalStateException {
		return ioController.getAvailableGames();
	}

	/**
	 * Create a new level for the game being authored. Saves the state of the
	 * current level being authored when the transition occurs.
	 *
	 * @param level
	 *            the number associated with the new level
	 */
	public void setLevel(int level) {
		assertValidLevel(level);
		currentLevel = level;
		if (level == getLevelSprites().size()) {
			initializeLevel();
		}
	}

	public TemplateProperties packageTemplateProperties(String templateName) {
		return getServerMessageUtils().packageTemplateProperties(templateName,
				serializationUtils.serializeElementTemplate(getTemplateProperties(templateName)));
	}

	public Collection<TemplateProperties> packageAllTemplateProperties() {
		Map<String, Map<String, String>> serializedTemplates = new HashMap<>();
		Map<String, Map<String, Object>> templates = getAllDefinedTemplateProperties();
		for (String templateName : templates.keySet()) {
			serializedTemplates.put(templateName,
					serializationUtils.serializeElementTemplate(templates.get(templateName)));
		}
		return getServerMessageUtils().packageAllTemplateProperties(serializedTemplates);
	}

	public Collection<ElementCost> packageAllElementCosts() {
		return getServerMessageUtils().packageAllElementCosts(getElementCosts());
	}

	@Override
	public Collection<NewSprite> getLevelSprites(int level) {
		Collection<NewSprite> levelSprites = getServerMessageUtils()
				.packageNewSprites(getFilteredSpriteIdMap(getLevelSprites().get(level)));
		return levelSprites;
	}

	@Override
	public int getLevelHealth(int level) {
		return getLevelHealths().get(level);
	}

	@Override
	public int getLevelPointQuota(int level) {
		return getLevelPointQuotas().get(level);
	}

	@Override
	public int getLevelTimeLimit(int level) {
		return getLevelTimeLimits().get(level);
	}

	protected int placeElement(String elementTemplateName, Point2D startCoordinates, Collection<?>... auxiliaryArgs)
			throws ReflectiveOperationException {
		Map<String, Object> auxiliarySpriteConstructionObjects = spriteQueryHandler
				.getAuxiliarySpriteConstructionObjectMap(ASSUMED_PLAYER_ID, startCoordinates,
						levelSpritesCache.get(currentLevel));
		for (Collection<?> auxiliaryArg : auxiliaryArgs) {
			auxiliarySpriteConstructionObjects.put(auxiliaryArg.getClass().getName(), auxiliaryArg);
		}
		auxiliarySpriteConstructionObjects.put("startPoint", startCoordinates);
		GameElement gameElement = gameElementFactory.generateElement(elementTemplateName,
				auxiliarySpriteConstructionObjects);
		return cacheAndCreateIdentifier(elementTemplateName, gameElement);
	}

	protected void cacheGeneratedSprite(GameElement gameElement) {
		List<GameElement> levelGameElements = levelSpritesCache.get(currentLevel);
		levelGameElements.add(gameElement);
	}

	protected IOController getIoController() {
		return ioController;
	}

	protected GameConditionsReader getGameConditionsReader() {
		return gameConditionsReader;
	}

	protected ServerMessageUtils getServerMessageUtils() {
		return serverMessageUtils;
	}

	protected GameElementIoHandler getGameElementIoHandler() {
		return gameElementIoHandler;
	}

	protected List<Map<String, Double>> getLevelStatuses() {
		return levelStatuses;
	}

	protected List<Map<String, String>> getLevelConditions() {
		return levelConditions;
	}

	protected List<String> getLevelDescriptions() {
		return levelDescriptions;
	}

	protected List<List<GameElement>> getLevelSprites() {
		return levelSpritesCache;
	}

	protected List<Set<String>> getLevelInventories() {
		return levelInventories;
	}

	protected List<Bank> getLevelBanks() {
		return levelBanks;
	}

	protected List<List<GameElement>> getLevelWaves() {
		return levelWaves;
	}

	protected List<Integer> getLevelHealths() {
		return levelHealths;
	}

	protected List<Integer> getLevelPointQuotas() {
		return levelPointQuotas;
	}

	protected List<Integer> getLevelTimeLimits() {
		return levelTimeLimits;
	}

	protected List<Map<String, Point2D>> getLevelWaveTemplates() {
		return levelWaveTemplates;
	}

	protected void loadLevelData(String saveName, int level, boolean originalGame) throws FileNotFoundException {
		loadGameStateElementsForLevel(saveName, level, originalGame);
		loadGameStateSettingsForLevel(saveName, level, originalGame);
		loadGameConditionsForLevel(saveName, level);
		loadGameDescriptionForLevel(saveName, level);
		loadGameBankForLevel(saveName, level, originalGame);
		loadGameInventoryElementsForLevel(saveName, level, originalGame);
		loadGameHealthForLevel(saveName, level, originalGame);
		loadGamePointsQuotaForLevel(saveName, level);
		loadGameTimeLimitsForLevel(saveName, level);
		// loadGameWavesForLevel(saveName, level);
	}

	protected GameElementFactory getGameElementFactory() {
		return gameElementFactory;
	}

	protected GameElementUpgrader getGameElementUpgrader() {
		return gameElementUpgrader;
	}

	protected SpriteQueryHandler getSpriteQueryHandler() {
		return spriteQueryHandler;
	}

	protected Map<Integer, GameElement> getSpriteIdMap() {
		return spriteIdMap;
	}

	protected int cacheAndCreateIdentifier(String elementTemplateName, GameElement gameElement) {
		spriteIdMap.put(spriteIdCounter.incrementAndGet(), gameElement);
		cacheGeneratedSprite(gameElement);
		return spriteIdCounter.get();
	}

	protected int cacheAndCreateIdentifier(GameElement gameElement) {
		spriteIdMap.put(spriteIdCounter.incrementAndGet(), gameElement);
		cacheGeneratedSprite(gameElement);
		return spriteIdCounter.get();
	}

	protected int getIdFromSprite(GameElement gameElement) throws IllegalArgumentException {
		Map<Integer, GameElement> spriteIdMap = getSpriteIdMap();
		for (Integer id : spriteIdMap.keySet()) {
			if (spriteIdMap.get(id) == gameElement) {
				return id;
			}
		}
		throw new IllegalArgumentException();
	}

	protected Collection<Integer> getIdsCollectionFromSpriteCollection(Collection<GameElement> gameElements) {
		return gameElements.stream().mapToInt(this::getIdFromSprite).boxed().collect(Collectors.toSet());
	}

	protected abstract void assertValidLevel(int level) throws IllegalArgumentException;

	private GameElement getElement(int elementId) throws IllegalArgumentException {
		if (!getSpriteIdMap().containsKey(elementId)) {
			throw new IllegalArgumentException();
		}
		return getSpriteIdMap().get(elementId);
	}

	private GameElement generatePlacedElement(String elementTemplateName, Point2D startCoordinates)
			throws ReflectiveOperationException {
		Map<String, Object> auxiliarySpriteConstructionObjects = spriteQueryHandler
				.getAuxiliarySpriteConstructionObjectMap(ASSUMED_PLAYER_ID, startCoordinates,
						levelSpritesCache.get(currentLevel));
		auxiliarySpriteConstructionObjects.put("startPoint", startCoordinates);
//		System.out.println("\n\n\n\n\n");
//		System.out.println(auxiliarySpriteConstructionObjects.keySet().toString());
//		System.out.println(auxiliarySpriteConstructionObjects.values().toString());
//		System.out.println("\n\n\n\n\n");
		GameElement element = gameElementFactory.generateElement(elementTemplateName,
				auxiliarySpriteConstructionObjects);
		return element;
	}

	private Collection<GameElement> loadGameStateElementsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		List<GameElement> loadedGameElements = ioController.loadGameStateElements(savedGameName, level, originalGame);
		for (GameElement loadedGameElement : loadedGameElements) {
			spriteIdMap.put(spriteIdCounter.getAndIncrement(), loadedGameElement);
			loadedGameElement.setX(loadedGameElement.getX());
			loadedGameElement.setY(loadedGameElement.getY());
		}
		addOrSetLevelData(levelSpritesCache, loadedGameElements, level);
		return loadedGameElements;
	}

	protected Map<Integer, GameElement> getFilteredSpriteIdMap(Collection<GameElement> spritesToFilter) {
		return getSpriteIdMap().entrySet().stream()
				.filter(spriteEntry -> spritesToFilter.contains(spriteEntry.getValue()))
				.collect(Collectors.toMap(spriteEntry -> spriteEntry.getKey(), spriteEntry -> spriteEntry.getValue()));
	}

	protected LevelInitialized packageStateChange(Collection<GameElement> oldGameElements) {

		return LevelInitialized.newBuilder()
				.setSpritesAndStatus(serverMessageUtils.packageUpdates(getSpriteIdMap(), new HashMap<>(),
						getFilteredSpriteIdMap(oldGameElements), false, false, false, false, getResourceEndowments(),
						getCurrentLevel()))
				.setInventory(packageInventory()).build();
	}

	private void loadGameStateSettingsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		Map<String, Double> loadedSettings = ioController.loadGameStateSettings(savedGameName, level, originalGame);
		addOrSetLevelData(levelStatuses, loadedSettings, level);
	}

	private void loadGameConditionsForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		Map<String, String> loadedLevelConditions = ioController.loadGameConditions(savedGameName, level);
		addOrSetLevelData(levelConditions, loadedLevelConditions, level);
	}

	private void loadGameInventoryElementsForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		Set<String> loadedInventories = ioController.loadGameInventories(savedGameName, level, originalGame);
		addOrSetLevelData(levelInventories, loadedInventories, level);
	}

	private void loadGameDescriptionForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelDescriptions, ioController.loadGameDescription(savedGameName, level), level);
	}

	private void loadGameBankForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelBanks, ioController.loadGameBank(savedGameName, level, originalGame), level);
	}

	private void loadGameHealthForLevel(String savedGameName, int level, boolean originalGame)
			throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelHealths, ioController.loadGameHealth(savedGameName, level, originalGame), level);
	}

	private void loadGamePointsQuotaForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelPointQuotas, ioController.loadGamePointQuotas(savedGameName, level), level);

	}

	private void loadGameTimeLimitsForLevel(String savedGameName, int level) throws FileNotFoundException {
		assertValidLevel(level);
		addOrSetLevelData(levelTimeLimits, ioController.loadGameTimeLimits(savedGameName, level), level);
	}

	private boolean isAuthoring() {
		// TODO - remove the forAuthoring param from ioController method so we don't
		// have to do this
		return this.getClass().equals(AuthoringController.class);
	}

	private <T> void addOrSetLevelData(List<T> allLevelData, T levelData, int level) {
		if (level == allLevelData.size()) {
			allLevelData.add(levelData);
		} else {
			allLevelData.set(level, levelData);
		}
	}

	private void initialize() {
		// To adjust for 1-indexing
		initializeLevel();
		setLevel(1);
	}

	private void initializeLevel() {
		getLevelStatuses().add(new HashMap<>());
		getLevelSprites().add(new ArrayList<>());
		getLevelInventories().add(new HashSet<>());
		getLevelDescriptions().add(new String());
		getLevelBanks().add(currentLevel > 0 ? getLevelBanks().get(currentLevel - 1).fromBank() : new Bank());
		getLevelWaves().add(new ArrayList<>());
		getLevelWaveTemplates().add(new HashMap<>());
		getLevelHealths().add(0);
		getLevelPointQuotas().add(DEFAULT_POINTS_QUOTA);
		getLevelTimeLimits().add(0);
		initializeLevelConditions();
	}

	private void initializeLevelConditions() {
		getLevelConditions().add(new HashMap<>());
		getLevelConditions().get(getCurrentLevel()).put(VICTORY, getDefaultVictoryCondition());
		getLevelConditions().get(getCurrentLevel()).put(DEFEAT, getDefaultDefeatCondition());
	}

	private String getDefaultVictoryCondition() {
		return new ArrayList<>(gameConditionsReader.getPossibleVictoryConditions()).get(2);
	}

	private String getDefaultDefeatCondition() {
		return new ArrayList<>(gameConditionsReader.getPossibleDefeatConditions()).get(0);
	}

	public int translateToOneBasedIndexing(int index) {
		return index+1;
	}
}
