package voogasalad_duvallinthistogether;

/**
 * Demonstrates use-case of saving the current game env from front end
 * @author radithya
 *
 */
public class LoadGameEnvUseCase  {

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
			 */
			@Override
			public void loadGameSettings(String gameName) {
			}
			
		}
		
		/**
		 * constructor for mock object
		 */
		public MockAuthoringEngine() {
			mockIOEngine = new MockIOEngine();
		}
		
		/**
		 * Mocked implementation of engine's saving game settings
		 */
		@Override
		public void loadGameSettings() {
			// Mock game name
			String gameName = "GAME"; 
			mockIOEngine.saveGameSettings(gameName);
		}
	}
	
	public LoadGameEnvUseCase() {
		mockAuthoringEngine = new MockAuthoringEngine();
	}
	
	/**
	 * Method called from GameAuthoringEnv front end
	 */
	private void loadGameEnv() {
		mockAuthoringEngine.loadGameSettings();
	}
	
}
