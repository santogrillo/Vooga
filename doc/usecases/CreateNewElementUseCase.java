package voogasalad_duvallinthistogether;

import java.util.Map;

/**
 * Demonstrates use-case of creating new element from the game env
 * 
 * @author radithya
 *
 */
public class CreateNewElementUseCase {

	private MockAuthoringEngine mockAuthoringEngine;

	/**
	 * Mock authoring engine for this use-case
	 * 
	 * @author radithya
	 *
	 */
	public class MockAuthoringEngine implements EditController {
		// Will add element to instance map of element type names to properties
		private Map<String, Map<String, String>> newlyCreatedElements;

		// Mock play engine
		public MockAuthoringEngine() {
			newlyCreatedElements = new HashMap<>();
		}

		/**
		 * Mocked implementation of element creation by EditController
		 * 
		 * @param name
		 * @param properties
		 */
		@Override
		public void createElement(String name, Map<String, String> properties) {
			// mock implementation
			newlyCreatedElements.put(name, properties);
		}
	}

	// Mock constructor
	public CreateNewElementUseCase() {
		mockAuthoringEngine = new MockAuthoringEngine();
	}

	/**
	 * Called by front end after aggregating properties through a series of prompts
	 * dialogs
	 * 
	 * @param name for element type to create
	 * @param properties a map of property name to value, i.e. {"image_url: <image_url>, "attack_damage": <attack_damage> ...}
	 */
	private void createElement(String name, Map<String, String> properties) {
		// Some front end code for displaying the element on the map - omitted
		// Stores element in engine's buffer, to be written to file after calling save
		// as demonstrated in SaveGameEnvUseCase
		mockAuthoringEngine.createElement(name, properties);
	}

}
