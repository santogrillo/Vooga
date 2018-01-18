package authoring_engine;

import java.util.Map;

// TODO - Better name? This is the engine module that handles requests from the authoring env, handling saving / loading to the backend io
public interface EditController {

	/**
	 * Define a new element type
	 * 
	 * @param name
	 *            the name of new element to be created
	 * @param properties
	 *            map of properties for the new element, of the form {"image_url":
	 *            <url>, "hp" : <hp>, ...}
	 */
	public void createElement(String name, Map<String, String> properties);

	/**
	 * Add a previously defined element type at a specified position on the map
	 * 
	 * @param name
	 *            the identifier for this previously created type
	 * @param x
	 *            xCoordinate of map location to place this element
	 * @param y
	 *            yCoordinate of map location to place this element
	 * @param level
	 *            level of the game this element is being added for
	 */
	public void addElement(String name, double x, double y, int level);

	/**
	 * 
	 * @param name
	 *            name of element type
	 * @param x
	 *            xCoordinate of previously created element
	 * @param y
	 *            yCoordinate of previously created element
	 * @param level
	 *            level of the game this element is being added for
	 * @param customProperties
	 *            map of properties to override for this specific instance
	 */
	public void updateElement(String name, double x, double y, int level, Map<String, String> customProperties);

	/**
	 * Set a top-level game property (e.g. lives, starting resources, etc)
	 * 
	 * @param property
	 *            name
	 * @param value
	 *            string representation of the value
	 */
	public void setGameParam(String property, String value);
}
