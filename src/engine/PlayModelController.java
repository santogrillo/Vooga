package engine;

import engine.behavior.movement.LocationProperty;
import javafx.geometry.Point2D;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Update;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Controls the model for a game being played. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface PlayModelController extends AbstractGameModelController {

    /**
     * Save the current state of a game being played.
     *
     * @param fileNameToSaveTo the name to assign to the save file
     */
    void saveGameState(String fileNameToSaveTo);

    /**
     * Load the detailed state of original (authored) game for a particular level,
     * including high-level information and elements present.
     *
     * @param saveName the name the save file was assigned
     * @param level    the level of the game which should be loaded
     * @throws IOException if the save name does not refer to a previously saved game state
     */
    LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException;

    /**
     * Load state of previously saved play
     *
     * @param savePlayStateName name of previously saved play state
     * @throws FileNotFoundException if no such saved play state exists
     */
    LevelInitialized loadSavedPlayState(String savePlayStateName) throws FileNotFoundException;

    /**
     * Run one cycle of update
     */
    Update update();

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
     * Ready to transition to next level
     *
     * @return
     */
    boolean isReadyForNextLevel();

    /**
     * Determine whether the game in-progress has been won.
     *
     * @return true if the game has been completed with a loss and false otherwise
     */
    boolean isWon();

    /**
     * Place a game element of previously defined (or default) type within the game.
     *
     * @param elementName      the template name for the element
     * @param startCoordinates the coordinates at which the element should be placed
     * @return a unique identifier for the sprite abstraction representing the game
     * element
     * @throws ReflectiveOperationException if the element could not be generated due to its template lacking a
     *                                      necessary property
     */
    NewSprite placeElement(String elementName, Point2D startCoordinates);

    /**
     * Upgrade an element that is placed in the game.
     *
     * @param elementId the unique identifier for the game element
     * @throws IllegalArgumentException     if the element has no remaining upgrades
     * @throws ReflectiveOperationException if the element could not be regenerated due to the upgrade template
     *                                      lacking a needed property
     */
    void upgradeElement(int elementId) throws IllegalArgumentException, ReflectiveOperationException;

    /**
     * Fetch all available game names and their corresponding descriptions
     *
     * @return map where keys are game names and values are game descriptions
     */
    Map<String, String> getAvailableGames();

    /**
     * Get a map of properties for an element template / model, so as to allow for
     * their displaying in a modification area of the display
     *
     * @param elementName the template name for the element
     * @return a map of properties for the template with this identifier
     * @throws IllegalArgumentException if the element name does not refer to a defined template
     */
    Map<String, Object> getTemplateProperties(String elementName) throws IllegalArgumentException;

    /**
     * Get map of all defined template names to their properties
     *
     * @return map of template names to properties of each template
     */
    Map<String, Map<String, Object>> getAllDefinedTemplateProperties();

    /**
     * Retrieve the inventory for the current level
     *
     * @return set of element names that can be placed in the current level
     */
    Set<String> getInventory();

	int getLevelHealth(int level);
	
	int getLevelPointQuota(int level);
	
	int getLevelTimeLimit(int level);
    
    /**
     * Get the high-level status of a game in-progress, notably points, lives, etc
     *
     * @return a map of relevant details to display or modify about the game
     * @deprecated
     */
    Map<String, Double> getStatus();

    /**
     * Retrieve information on the quantity of each resource left
     *
     * @return map of resource name to quantity of that resource left
     */
    Map<String, Double> getResourceEndowments();

    /**
     * Retrieve information on the cost of each element for the current level in
     * terms of the various resources
     *
     * @return map of element name to its cost in terms of each resource
     */
    Map<String, Map<String, Double>> getElementCosts();

    /**
     * Get the points value associated with (the destruction of) a particular element.
     *
     * @param elementId the unique identifier of the element being queried
     * @return the point cost associated with the element
     */
    double getElementPointValue(int elementId);

    /**
     * Get the elements of a game (represented as sprites) for a particular level.
     *
     * TODO - custom exception?
     *
     * @param level the level of the game which should be loaded
     * @return all the game elements (sprites) represented in the level
     * @throws IllegalArgumentException if there is no corresponding level in the current game
     */
    Collection<NewSprite> getLevelSprites(int level) throws IllegalArgumentException;

    /**
     * Get an object that allows for the tracking and setting of a particular element's location.
     *
     * @param elementId the unique identifier of the element
     * @return a settable object property for the location for the desired game element
     * @throws IllegalArgumentException if the given ID does not correspond to a placed game element
     */
    LocationProperty getElementLocationProperty(int elementId);
    
    /**
     * Prompts a particular element to fire it's projectile if a firing strategy is assigned
     * 
     * @param elementId
     */
    void triggerFire(int elementId);
    	
}
