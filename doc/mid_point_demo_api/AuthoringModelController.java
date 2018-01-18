package engine;

import util.path.PathList;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controls the model for a game being authored. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author Ben Schwennesen
 */
public interface AuthoringModelController {

	/**
	 * Save the current state of the current level a game being authored.
	 *
	 * @param saveName
	 *            the name to assign to the save file
	 */
	void saveGameState(File saveName);

	/**
	 * Load the detailed state of a game for a particular level, including
	 * high-level information and elements present.
	 *
	 * @param saveName
	 *            the name used to save the game authoring data
	 * @param level
	 *            the level of the game which should be loaded
	 * @throws IOException
	 *             if the save name does not refer to existing files
	 */
	void loadOriginalGameState(String saveName, int level) throws IOException;

	/**
	 * Export a fully authored game, including all levels, into an executable file.
	 */
	void exportGame();

	/**
	 * Create a new level for the game being authored. Saves the state of the
	 * current level being authored when the transition occurs.
	 *
	 * @param level
	 *            the number associated with the new level
	 */
	void createNewLevel(int level);

	/**
	 * Delete the previously created level.
	 *
	 * @param level
	 *            the level to delete
	 * @throws IllegalArgumentException
	 *             if level does not exist
	 */
	void deleteLevel(int level) throws IllegalArgumentException;

	/**
	 * Get the top-level configuration options for a game element definition.
	 *
	 * @return a map from the name of the configuration option to set to a list of
	 *         choices for that option
	 */
	Map<String, List<String>> getElementBaseConfigurationOptions();

	/**
	 * Get auxiliary configuration elements for a game element, based on top-level
	 * configuration choices.
	 *
	 * @return a map from the name of the configuration option to its class type
	 */
	Map<String, Class> getAuxiliaryElementConfigurationOptions(Map<String, String> baseConfigurationChoices);

	/**
	 * Define a new type of element for the game being authored. Elements of this
	 * type will be created by the model based on its properties, assuming defaults
	 * where necessary. This method should not be used for updating properties of an
	 * existing template, the updateElementDefinition method should be used for that
	 * instead.
	 *
	 * @param elementName
	 *            the template name assigned to this element, for future reuse of
	 *            the properties
	 * @param properties
	 *            a map containing the properties of the element to be created
	 * @throws IllegalArgumentException
	 *             if the template already exists.
	 */
	void defineElement(String elementName, Map<String, String> properties) throws IllegalArgumentException;

	/**
	 * Update an existing template by overwriting the specified properties to their
	 * new specified values. Should not be used to create a new template, the
	 * defineElement method should be used for that.
	 * 
	 * @param elementName
	 *            the name of the template to be updated
	 * @param propertiesToUpdate
	 *            the properties to update
	 * @param retroactive
	 *            whether previously created elements of this type must have their
	 *            properties updated
	 * 
	 * @throws IllegalArgumentException
	 *             if the template does not already exist
	 */
	void updateElementDefinition(String elementName, Map<String, String> propertiesToUpdate, boolean retroactive)
			throws IllegalArgumentException;

	/**
	 * Delete a previously defined template
	 * 
	 * @param elementName
	 *            name of the template to delete
	 * @throws IllegalArgumentException
	 *             if the template does not already exist
	 */
	void deleteElementDefinition(String elementName) throws IllegalArgumentException;

	/**
	 * Place a game element of previously defined type within the game.
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
	 * Add element of given name
	 * 
	 * @param elementName
	 */
	void addElementToInventory(String elementName);

	/*
	 * Place a game element of previously defined type within the game which follows
	 * a path defined in the authoring environment as it moves.
	 *
	 * @param elementName the template name for the element
	 * 
	 * @param pathList a list of points the object should target as it moves
	 * 
	 * @return a unique identifier for the sprite abstraction representing the game
	 * element
	 */
	int placePathFollowingElement(String elementName, PathList pathList);

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
	 * Retrieve information on the cost of each element in terms of the various
	 * resources
	 * 
	 * @return map of element name to its cost in terms of each resource
	 */
	Map<String, Map<String, Double>> getElementCosts();

	/**
	 * Move a previously created game element to a new location.
	 *
	 * @param elementId
	 *            elementId the unique identifier for the element
	 * @param xCoordinate
	 *            the new horizontal position of the element within the game
	 * @param yCoordinate
	 *            the new vertical position of the element within the game
	 */
	void moveElement(int elementId, double xCoordinate, double yCoordinate);

	/**
	 * Update the properties of a particular game element, without changing the
	 * definition of its type.
	 *
	 * @param elementId
	 *            the unique identifier for the element
	 * @param propertiesToUpdate
	 *            a map containing the new properties of the element
	 */
	void updateElementProperties(int elementId, Map<String, String> propertiesToUpdate);

	/**
	 * Delete a previously created game element.
	 *
	 * @param elementId
	 *            the unique identifier for the element
	 */
	void deleteElement(int elementId);

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	Map<String, String> getAvailableGames();

	/**
	 * Get a map of properties for a particular game element, so as to allow for
	 * their displaying in a modification area of the display.
	 *
	 * @param elementId
	 *            the unique identifier for the game element
	 * @return a map of properties for the element with this identifier
	 * @throws IllegalArgumentException
	 *             if the element ID does not refer to a created element
	 */
	Map<String, String> getElementProperties(int elementId) throws IllegalArgumentException;

	/**
	 * Get a map of properties for an element template / model, so as to allow for
	 * their displaying in a modification area of the display
	 * 
	 * @param elementName
	 *            the template name for the element
	 * @return a map of properties for the template with this identifier
	 * @throws IllegalArgumentException
	 *             if the element name does not refer to a defined template
	 */
	Map<String, String> getTemplateProperties(String elementName) throws IllegalArgumentException;

	/**
	 * Get map of all defined template names to their properties
	 * 
	 * @return map of template names to properties of each template
	 */
	Map<String, Map<String, String>> getAllDefinedTemplateProperties();

	Map<String, Double> getResourceEndowments();

	/**
	 * Set the name of the game being authored.
	 *
	 * @param gameName
	 *            the name of the game
	 */
	void setGameName(String gameName);

	/**
	 * Set the description of a game being authored.
	 *
	 * @param gameDescription
	 *            the description authored for the game
	 */
	void setGameDescription(String gameDescription);

	/**
	 * Set the victory condition for the current level of the game being authored
	 * 
	 * @param conditionIdentifier
	 *            the description of the victory condition, which can be mapped to a
	 *            boolean state function
	 */
	void setVictoryCondition(String conditionIdentifier);

	/**
	 * Set the defeat condition for the current level of the game being authored
	 * 
	 * @param conditionIdentifier
	 *            the description of the defeat condition, which can be mapped to a
	 *            boolean state function
	 */
	void setDefeatCondition(String conditionIdentifier);

	/**
	 * Set a top-level game status property (e.g. lives, starting resources, etc)
	 *
	 * @param property
	 *            name of the property to set
	 * @param value
	 *            string representation of the property's new value
	 */
	void setStatusProperty(String property, Double value);

	/**
	 * Set the resource endowments for the current level
	 * 
	 * @param resourceEndowments
	 *            map of resource name to amount of that resource to begin that
	 *            level with
	 */
	void setResourceEndowments(Map<String, Double> resourceEndowments);

	/**
	 * Set the resource endowment of a specific resource name
	 * @param resourceName
	 * @param newResourceEndowment
	 */
	void setResourceEndowment(String resourceName, double newResourceEndowment);
	
	/**
	 * Set the cost of an element in terms of various resources
	 * 
	 * @param elementName
	 *            the template name for the element
	 * @param unitCosts
	 *            map of resource name to cost in terms of that resource for this
	 *            element
	 */
	void setUnitCost(String elementName, Map<String, Double> unitCosts);

	/**
	 * Set the behavior and parameters of the wave
	 * 
	 * @param waveProperties
	 *            a map containing the properties of the wave to be created
	 * @param elementNames
	 *            name of elements to spawn
	 * @param spawningPoint
	 *            the point at which to spawn the wave
	 */
	void setWaveProperties(Map<String, String> waveProperties, Collection<String> elementNamesToSpawn,
			Point2D spawningPoint);

	/**
	 * Retrieve a collection of descriptions of the possible victory conditions
	 * 
	 * @return a collection of strings describing the possible victory conditions
	 *         that can be assigned for a given level
	 */
	Collection<String> getPossibleVictoryConditions();

	/**
	 * Retrieve a collection of descriptions of the possible defeat conditions
	 * 
	 * @return a collection of strings describing the possible defeat conditions
	 *         that can be assigned for a given level
	 */
	Collection<String> getPossibleDefeatConditions();

}
