
public interface PlayIOController {

	/**
	 * Save state of currently played game - assumes only 1 game in play for a given
	 * engine at a time?
	 */
	public void saveGameState();

	/**
	 * Load collection of elements for a previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @return a collection of elements which can be saved in the engine and passed
	 *         to the front end
	 */
	public Collection<TowerDefenseElement> loadGameStateElements(String savedGameName);

	/**
	 * Load top-level game status settings (lives left, resources left, etc.) for a
	 * previously saved game state
	 * 
	 * @param savedGameName
	 *            the name used to save the game state
	 * @return map of state keys to values
	 */
	public Map<String, String> loadGameStateSettings(String savedGameName);

	/**
	 * Fetch all available game names and their corresponding descriptions
	 * 
	 * @return map where keys are game names and values are game descriptions
	 */
	public Map<String, String> getAvailableGames();

}
