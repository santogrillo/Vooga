package io;

public interface PlayPersistence {

	/**
	 * 
	 * @param fileName chosen file ame for saved game state
	 * @param serializedRepresentation of current game
	 */
	public void saveGameState(String fileName, String serializedRepresentation);
	
	/**
	 * 
	 * @param fileName file name for game to be loaded
	 */
	public String loadGameState(String fileName);
}
