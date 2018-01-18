package authoring_engine;

public interface AuthoringIOController {

	/**
	 * Flush the current buffer of game settings to a folder of json / xml /
	 * properties files with game name as folder name
	 */
	public void saveGameSettings();

	/**
	 * Load the settings of a previously (partially) authored game into engine for
	 * display by controller, returning root element from which all other elements
	 * can be obtained
	 * 
	 * @param gameName
	 *            the name of the game whose settings are to be loaded
	 * @return collection of all elements authored for this game
	 */
	public Collection<TowerDefenseElement> loadGameElements(String gameName);
	
	/**
	 * Load top-level game settings (starting lives, resources, etc.)
	 * @param gameName
	 * @return
	 */
	public Map<String, String> loadGameSettings(String gameName);

}
