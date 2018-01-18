package voogasalad_duvallinthistogether;

/**
 * Demonstrates use-case of showing a heads-up display in the player
 * 
 * @author radithya
 *
 */
public class HeadsUpDisplayUseCase {

	private MockPlayEngine mockPlayEngine;

	/**
	 * Mock play engine for this use-case
	 * 
	 * @author radithya
	 *
	 */
	public class MockPlayEngine implements Behavior {

		private Map<String, String> gameStatus;

		public MockPlayEngine() {
			gameStatus = new HashMap<>();
		}

		@Override
		public Map<String, String> getStatus() {
			return gameStatus;
		}
	}

	public HeadsUpDisplayUseCase() {
		mockPlayEngine = new MockPlayEngine();
	}

	/**
	 * Just a naive sample mock implementation - a better implementation could use
	 * some kind of binding between this map and the heads-up display, or make it
	 * observable
	 */
	private void updateHeadsUpDisplay() {
		gameStatus = mockPlayEngine.getStatus();
		// FRONT END CODE FOR MANIPULATING THE GUI OBJECT (Panel, etc) using this map
	}

}
