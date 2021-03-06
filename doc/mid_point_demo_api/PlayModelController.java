package engine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

/**
 * Controls the model for a game being played. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface PlayModelController {

	/**
	 * Save the current state of a game being played.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	void saveGameState(String saveName);

	/**
	 * Load the detailed state of original (authored) game for a particular level,
	 * including high-level information and elements present.
	 *
	 * @param saveName
	 *            the name the save file was assigned
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws IOException
	 * 			  if the save name does not refer to a previously saved game state
	 */
	void loadOriginalGameState(String saveName, int level) throws IOException;

	/**
	 * Load state of previously saved play
	 * 
	 * @param savePlayStateName
	 *            name of previously saved play state
	 * @throws FileNotFoundException
	 *             if no such saved play state exists
	 */
	void loadSavedPlayState(String savePlayStateName) throws FileNotFoundException;

	/**
	 * Run one cycle of update
	 */
	void update();

	/**
	 * Pause the game.
	 */
	void pause();

	/**
	 * Resume the (paused) game.
	 */
	void resume();

	/**
	 * Determine whether the game in-progress has been lost.
	 *
	 * @return true if the game has been completed with a loss and false otherwise
	 */
	boolean isLost();

	
	boolean isLevelCleared();
	
	/**
	 * Determine whether the game in-progress has been won.
	 *
	 * @return true if the game has been completed with a loss and false otherwise
	 */
	boolean isWon();

	/**
	 * Place a game element of previously defined (or default) type within the game.
	 *
	 * @param elementName
	 *            the template name for the element
	 * @param startCoordinates
	 *            the coordinates at which the element should be placed
	 * @return a unique identifier for the sprite abstraction representing the game
	 *         element
	 */
	int placeElement(String elementName, Point2D startCoordinates);
	
	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	Map<String, String> getAvailableGames();

	/**
	 * Get map of all defined template names to their properties
	 * 
	 * @return map of template names to properties of each template
	 */
	Map<String, Map<String, String>> getAllDefinedTemplateProperties();
	
	/**
	 * Retrieve the inventory for the current level
	 * 
	 * @return set of element names that can be placed in the current level
	 */
	Set<String> getInventory();
	
	/**
	 * Get the ImageView corresponding to a particular spriteId
	 * 
	 * @param spriteId
	 *            id of the sprite whose image is to be retrieved, previously
	 *            generated by controller
	 * @return ImageView representing the GameElement
	 */
	ImageView getRepresentationFromSpriteId(int spriteId);

	/**
	 * Get the high-level status of a game in-progress, notably points, lives, etc
	 * 
	 *
	 * @return a map of relevant details to display or modify about the game
	 */
	Map<String, Double> getStatus();

	/**
	 * Retrieve information on the quantity of each resource left
	 * 
	 * @return map of resource name to quantity of that resource left
	 */
	Map<String, Double> getResourceEndowments();

	/**
	 * Retrieve information on the cost of each element in terms of the various
	 * resources
	 * 
	 * @return map of element name to its cost in terms of each resource
	 */
	Map<String, Map<String, Double>> getElementCosts();

	/**
	 * Get the elements of a game (represented as sprites) for a particular level.
	 *
	 * TODO - custom exception?
	 *
	 * @param level
	 *            the level of the game which should be loaded
	 * @return all the game elements (sprites) represented in the level
	 * @throws IllegalArgumentException
	 *             if there is no corresponding level in the current game
	 */
	Collection<Integer> getLevelSprites(int level) throws IllegalArgumentException;
}
