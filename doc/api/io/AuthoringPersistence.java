package io;

public interface AuthoringPersistence {

	/**
	 * Save the serialized representation of currently authored game to json / xml
	 * file of given name
	 * 
	 * @param gameName
	 *            name of game to be saved
	 * @param serializedRepresentation
	 *            (json / xml) string representation of game
	 */
	public void saveGameSettings(String gameName, String serializedRepresentation);

	
	/**
	 * 
	 * @param gameName name of game to be loaded
	 * @return String (json / xml) which can be decoded into objects
	 */
	public String loadGameSettings(String gameName);

}
