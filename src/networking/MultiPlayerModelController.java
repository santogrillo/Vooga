package networking;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;

/**
 * Interface to capture any additional metadata related to multiplayer
 * environment, wraps around PlayModelController
 * 
 * @author radithya
 *
 */
interface MultiPlayerModelController {

	/**
	 * Initialize a game room for the game of the given name, retrieving a string
	 * identifier for this game room
	 * 
	 * @param gameName
	 *            name of the game to load
	 * @return unique string identifier (among currently active game rooms) for the
	 *         newly created game room
	 */
	String createGameRoom(int clientId, String gameName);

	/**
	 * Join the currently active game room identified by the given game room name,
	 * with the given username
	 * 
	 * @param gameRoomName
	 *            name of a currently active game room
	 * @param userName
	 *            username to use
	 * @return true if successfully joined, false otherwise (game room not active,
	 *         userName taken)
	 */
	boolean joinGameRoom(int clientId, String gameRoomName, String userName);

	/**
	 * Launch the game for the given game room name, with the currently joined
	 * players. Closes the game for joining
	 * 
	 * @param gameRoomName
	 *            name of a currently active game room
	 */
	void launchGameRoom(int clientId, String gameRoomName);

	/**
	 * Retrieve set of names of currently active game rooms that can be joined
	 * 
	 * @return set of string identifiers for currently active game rooms
	 */
	Set<String> getGameRooms();

	/**
	 * Retrieve set of usernames for the game
	 * 
	 * @param gameRoomName
	 *            name of game room
	 * @return set of usernames
	 */
	Set<String> getPlayerNames(int clientId, String gameRoomName);

	void update(String gameRoomName);

	void pause(int clientId);

	void resume(int clientId);

	boolean isLost(int clientId);

	boolean isLevelCleared(int clientId);

	boolean isWon(int clientId);

	int placeElement(int clientId, String elementName, Point2D startCoordinates);

	Map<String, String> getAvailableGames();

	Map<String, String> getTemplateProperties(int clientId, String elementName) throws IllegalArgumentException;

	Map<String, Map<String, String>> getAllDefinedTemplateProperties(int clientId);

	Set<String> getInventory(int clientId);

	Map<String, Double> getStatus(int clientId);

	Map<String, Double> getResourceEndowments(int clientId);

	Map<String, Map<String, Double>> getElementCosts(int clientId);

	Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException;

	void saveGameState(File fileToSaveTo) throws UnsupportedOperationException;

}
