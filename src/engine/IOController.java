package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import engine.game_elements.GameElement;
import util.io.GamePersistence;
import util.io.SerializationUtils;

/**
 * Gateway of authoring and play modules to I/O through engine. Encapsulates
 * shared logic for authoring and play use-cases
 * 
 * @author radithya
 *
 */
public class IOController {

	public static final String AUTHORING_GAMES_FOLDER = "authoring/";
	public static final String PLAY_GAMES_FOLDER = "play/";
	public static final int FIRST_LEVEL = 1;

	private SerializationUtils serializationUtils;
	private GamePersistence gamePersistence;

	public IOController(SerializationUtils serializationUtils) {
		this.serializationUtils = serializationUtils;
		gamePersistence = new GamePersistence();
	}

	/**
	 * Save game state
	 * 
	 * @param savedGameName
	 *            name for game state to be saved to
	 * @param gameDescription
	 *            description for game
	 * @param currentLevel
	 *            level for which game state is saved
	 * @param status
	 *            top-level status key-value pairs for heads-up display (player) or
	 *            settings (authoring)
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 */
	public void saveGameState(String savedGameName, String gameDescription, int currentLevel,
			Map<String, String> levelConditions, Bank levelBank, List<GameElement> levelGameElements,
			Set<String> levelInventories, int levelHealth, int levelPointsQuota, int levelTimeLimits, Map<String, Double> status, boolean forAuthoring) {
		// First extract string from file through io module
		String serializedGameState = serializationUtils.serializeGameData(gameDescription, levelConditions, levelBank,
				currentLevel, status, levelGameElements, levelInventories, levelHealth, levelPointsQuota, levelTimeLimits);
		String resolvedGameName = getResolvedGameName(savedGameName, forAuthoring);
		gamePersistence.saveGameState(resolvedGameName, serializedGameState);
	}

	// TODO - throw custom exception
	/**
	 * Load collection of elements for a previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 * @return a collection of elements which can be saved in the engine and passed
	 *         to the front end
	 * @throws FileNotFoundException
	 */
	public List<GameElement> loadGameStateElements(String savedGameName, int level, boolean forAuthoring)
			throws FileNotFoundException {
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, forAuthoring));
		// deserialize string into map through utils module
		List<GameElement> levelGameElements = serializationUtils.deserializeGameSprites(serializedGameData, level);
		return levelGameElements;
	}

	// TODO - throw custom exception
	/**
	 * Load top-level game status settings (lives left, resources left, etc.) for a
	 * previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 * @return map of state keys to values
	 * @throws FileNotFoundException
	 */
	public Map<String, Double> loadGameStateSettings(String savedGameName, int level, boolean forAuthoring)
			throws FileNotFoundException {
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, forAuthoring));
		// deserialize string into map through utils module
		return serializationUtils.deserializeGameStatus(serializedGameData, level);
	}

	/**
	 * Load conditions (victory, defeat, etc.) for the game
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param level
	 *            the level whose conditions are to be loaded
	 * @return map of condition key (e.g. victory, defeat) to string from which
	 *         boolean function can be dispatched through reflection
	 * @throws FileNotFoundException
	 */
	public Map<String, String> loadGameConditions(String savedGameName, int level) throws FileNotFoundException {
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, true));
		// deserialize string into map through utils module
		return serializationUtils.deserializeGameConditions(serializedGameData, level);
	}

	/**
	 * Load bank for the game level
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param level
	 *            the level whose bank is to be loaded
	 * @param forAuthoring
	 *            true if loading original authored game, false if loading saved
	 *            play-state
	 * @return bank instance as saved for that level
	 * @throws FileNotFoundException
	 */
	public Bank loadGameBank(String savedGameName, int level, boolean forAuthoring) throws FileNotFoundException {
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, forAuthoring));
		return serializationUtils.deserializeGameBank(serializedGameData, level);
	}

	public String loadGameDescription(String savedGameName, int level) throws FileNotFoundException {
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, true));
		// deserialize string into map through utils module
		return serializationUtils.deserializeGameDescription(serializedGameData, level);
	}

	public Set<String> loadGameInventories(String savedGameName, int level, boolean forAuthoring)
			throws FileNotFoundException {
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, forAuthoring));
		return serializationUtils.deserializeGameInventories(serializedGameData, level);
	}

	public int loadGameHealth(String savedGameName, int level, boolean forAuthoring) throws FileNotFoundException {
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, forAuthoring));
		return serializationUtils.deserializeGameHealth(serializedGameData, level);
	}
	
	public int loadGamePointQuotas(String savedGameName, int level) throws FileNotFoundException {
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, true));
		return serializationUtils.deserializeGamePoints(serializedGameData, level);
	}
	
	public int loadGameTimeLimits(String savedGameName, int level) throws FileNotFoundException {
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, true));
		return serializationUtils.deserializeGameTimeLimits(serializedGameData, level);
	}
	
	/**
	 * Get the number of levels for this game
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 * @return number of levels that currently exist in this game
	 * @throws FileNotFoundException
	 */
	public int getNumberOfLevelsForGame(String savedGameName, boolean forAuthoring) throws FileNotFoundException {
		// First extract string from file through io module
		String serializedGameData = gamePersistence.loadGameState(getResolvedGameName(savedGameName, forAuthoring));
		return serializationUtils.getNumLevelsFromSerializedGame(serializedGameData);
	}

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 * @throws IllegalStateException
	 *             if no games are found
	 */
	public Map<String, String> getAvailableGames() throws IllegalStateException {
		// retrieve set of files from authoring folder through io module
		Map<String, String> authoredGameSerializations = getAuthoredGameSerializations();
		Map<String, String> availableGames = new HashMap<>();
		// for each file, extract description through util module
		for (String authoredGameName : authoredGameSerializations.keySet()) {
			availableGames.put(authoredGameName,
					// Level description of 1st level of game assumed to be game description?
					serializationUtils.deserializeGameDescription(authoredGameSerializations.get(authoredGameName),
							FIRST_LEVEL));
		}
		return availableGames;
	}

	/**
	 * 
	 * @param fileName
	 *            chosen file name for saved authoring game state, create if file
	 *            does not exist, overwrite if file exists
	 * @param serializedRepresentation
	 *            of current game
	 */
	public void saveAuthoringGameState(String fileName, String serializedRepresentation) {
		gamePersistence.saveGameState(AUTHORING_GAMES_FOLDER + fileName, serializedRepresentation);
	}

	/**
	 * 
	 * @param fileName
	 *            chosen file name for saved play game state, create if file does
	 *            not exist, overwrite if file exists
	 * @param serializedRepresentation
	 *            of current game
	 */
	public void savePlayGameState(String fileName, String serializedRepresentation) {
		gamePersistence.saveGameState(PLAY_GAMES_FOLDER + fileName, serializedRepresentation);
	}

	// TODO - throw custom exception
	/**
	 * 
	 * @param fileName
	 *            file name for authored game to be loaded
	 */
	public String loadAuthoringGameState(String fileName) throws FileNotFoundException, IllegalArgumentException {
		return gamePersistence.loadGameState(AUTHORING_GAMES_FOLDER + fileName);
	}

	// TODO - throw custom exception
	/**
	 * 
	 * @param fileName
	 *            file name for played game to be loaded
	 */
	public String loadPlayGameState(String fileName) throws FileNotFoundException {
		return gamePersistence.loadGameState(PLAY_GAMES_FOLDER + fileName);
	}

	/**
	 * Retrieve a map of authored game names to their description
	 * 
	 * @return map of {game_name : game_description}
	 */
	public Map<String, String> getAuthoredGameSerializations() throws IllegalStateException {
		Map<String, String> authoredGameSerializationMap = new HashMap<>();
		// iterate over file names in authored_games folder, serialize each
		File authoredGamesDirectory = new File(AUTHORING_GAMES_FOLDER);
		// String decodedPath = URLDecoder.decode(path, "UTF-8");
		File[] authoredGames = authoredGamesDirectory.listFiles();
		if (authoredGames == null) {
			throw new IllegalStateException();
		}
		for (File authoredGame : authoredGames) {
			String authoredGameName = authoredGame.getName();
			// FileNotFoundException would not make sense here as we are iterating through
			// files found in directory, so safe to ignore
			try {
				authoredGameSerializationMap.put(authoredGameName, loadAuthoringGameState(authoredGameName));
			} catch (FileNotFoundException | IllegalArgumentException e) {
				continue;
			}
		}
		return authoredGameSerializationMap;
	}

	/**
	 * Get serialization of a level's data
	 *
	 * @param level
	 *            level to get a serialization of
	 * @param levelDescription
	 *            description of level as set by authoring engine
	 * @param levelStatus
	 *            top-level status metrics of game
	 * @param levelGameElements
	 *            the template-mapped game elements present in the level
	 * @return string representing serialization of level's data
	 */
	public String getLevelSerialization(int level, String levelDescription, Map<String, String> levelConditions,
			Bank levelBank, Map<String, Double> levelStatus, List<GameElement> levelGameElements,
			Set<String> levelInventories, int levelHealth, int levelPointsQuota, int levelTimeLimits) {
		return serializationUtils.serializeLevelData(levelDescription, levelConditions, levelBank, levelStatus,
				levelGameElements, levelInventories, levelHealth, levelPointsQuota, levelTimeLimits, level);
	}

	public Map<String, String> getWaveSerialization(Map<String, ?> waveProperties) {
		return serializationUtils.serializeWaveProperties(waveProperties);
	} 

	
	/**
	 * Save game state for multiple levels using mapping of level to serialized data
	 * for level Especially useful for authoring use-case where a (partially) built
	 * game with many levels of data has to be saved, differentiating between levels
	 * 
	 * @param saveName
	 *            name for game state to be saved to
	 * @param serializedLevelsData
	 *            map of level to serialized data for level
	 * @param forAuthoring
	 *            true if for authoring, false if for play - TODO - more flexible
	 *            approach? reflection?
	 */
	public void saveGameStateForMultipleLevels(String saveName, Map<Integer, String> serializedLevelsData,
			boolean forAuthoring) {
		String serializedGameData = serializationUtils.serializeLevelsData(serializedLevelsData);
		 gamePersistence.saveGameState(getResolvedGameName(saveName, forAuthoring),
		 serializedGameData);
		//gamePersistence.saveGameState(saveName, serializedGameData);
	}
	

	private String getResolvedGameName(String savedGameName, boolean forAuthoring) {
		String prefix = forAuthoring ? AUTHORING_GAMES_FOLDER : PLAY_GAMES_FOLDER;
		return prefix + savedGameName;
	}

}
