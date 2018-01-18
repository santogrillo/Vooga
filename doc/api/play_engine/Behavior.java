package play_engine;

public interface Behavior {

	/**
	 * Run the game loop for the given number of cycles
	 * 
	 * @param cycles
	 *            the number of cycles
	 */
	public void update(int cycles);

	/**
	 * Place element of specified name at specified location
	 * 
	 * @param elementName
	 *            name of element which can be used by ElementFactory to construct a
	 *            TowerDefenseElement using properties / json data files
	 * @param x
	 *            xCoordinate where element was placed
	 * @param y
	 *            yCoordinate where element was placed
	 * @return the created element
	 */
	public TowerDefenseElement placeElement(String elementName, double x, double y);

	/**
	 * Get lives left
	 * 
	 * @return number of lives left
	 */
	public int getLives();

	/**
	 * Retrieve the amount of each resource left
	 * 
	 * @return map of resource name to amount left
	 */
	public Map<String, Integer> getResources();

	/**
	 * Query the current level of the game
	 * 
	 * @return the integer corresponding to the game's current level
	 */
	public int getCurrentLevel();
	
	/**
	 * Query current play status (lives, kills, resources, all top-level metrics)
	 * @return map of parameter name to value
	 */
	public Map<String, String> getStatus();

	/**
	 * Query whether the game is currently in play
	 * 
	 * @return true if in play, false if over / paused
	 */
	public boolean isInPlay();

	/**
	 * Query whether game has been won
	 * 
	 * @return true if won, false otherwise
	 */
	public boolean isWon();

	/**
	 * Query whether game has been lost
	 * 
	 * @return true if lost, false otherwise
	 */
	public boolean isLost();

	/**
	 * Query whether the current level has been cleared (if so, game will be paused
	 * until resume() is called )
	 * 
	 * @return true if current level is cleared and game is paused, false otherwise
	 */
	public boolean isLevelCleared();

	/**
	 * Called to get current collection of events
	 * 
	 * @return current collection of elements
	 */
	public Collection<TowerDefenseElement> getCurrentElements();

	/**
	 * Pause the game
	 */
	public void pause();

	/**
	 * Resume the game
	 */
	public void resume();
}
