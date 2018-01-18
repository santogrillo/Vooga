package voogasalad_duvallinthistogether;

import SaveGameEnvUseCase.MockAuthoringEngine.MockIOEngine;

/**
 * Demonstrates use-case of loading the current game env from front end
 * @author radithya
 *
 */
public class SaveGameEnvUseCase {

	private MockAuthoringEngine mockAuthoringEngine;
	
	/**
	 * Mock authoring engine for this use-case
	 * @author radithya
	 *
	 */
	public class MockAuthoringEngine implements AuthoringIOController {
		
		private MockIOEngine mockIOEngine;
		
		public class MockIOEngine implements AuthoringPersistence {

			/**
			 * constructor for mock io engine
			 */
			public MockIOEngine() {
			}
			
			/**
			 * Mock implementation
			 * @param gameName
			 * @param serializedRepresentation
			 */
			@Override
			public void saveGameSettings(String gameName, String serializedRepresentation) {
			}
			
		}
		
		public MockIOEngine() {
			mockIOEngine = new MockIOEngine();
		}
	
		/**
		 * Mocked implementation
		 */
		@Override
		public void saveGameSettings() {
			// Mock game name
			String gameName = "GAME"; 
			// Mock serialization - will call a serializing method in reality
			String serializedRepresentation = "";
			mockIOEngine.saveGameSettings(gameName, serializedRepresentation);
		}
		
	}

	public SaveGameEnvUseCase() {
		mockAuthoringEngine = new MockAuthoringEngine();
	}
	
	/**
	 * Called by authoring to save current game env
	 */
	private void saveGameEnv() {
		mockAuthoringEngine.saveGameSettings();
	}

}
