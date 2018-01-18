package voogasalad_duvallinthistogether;

import java.util.Map;

/**
 * Demonstrates use-case of placing an element (like a tower) at a position
 * during a game
 * 
 * @author radithya
 *
 */
public class PlaceElementInGameUseCase {

	private MockPlayEngine mockPlayEngine;

	/**
	 * Mock play engine for this use-case
	 * 
	 * @author radithya
	 *
	 */
	public class MockPlayEngine implements Behavior {

		private MockElementFactory mockElementFactory;
		private Set<TowerDefenseElement> elements;

		/**
		 * Mock element factory for this use-case
		 * 
		 * @author radithya
		 *
		 */
		public class MockElementFactory {

			public MockElementFactory() {
			}

			/**
			 * Use reflection and/or data files to construct element of appropriate
			 * properties from name
			 * 
			 * @param elementName
			 *            name of element to be created, previously defined in authoring and
			 *            found in data file
			 * @return instance of the appropriate TowerDefenseElement sub-class
			 */
			public TowerDefenseElement createElementFromName(String elementName) {
				// MOCKED IMPLEMENTATION
				return null;
			}
		}

		// Mock constructor for mock object
		public MockPlayEngine() {
			mockElementFactory = new MockElementFactory();
			elements = new HashSet<>();
		}

		/**
		 * Mock implementation (params documented in interface in doc/api)
		 * 
		 * @param elementName
		 * @param x
		 * @param y
		 * @return
		 */
		@Override
		public TowerDefenseElement placeElement(String elementName, double x, double y) {
			TowerDefenseElement createdElement = mockElementFactory.createElementFromName(elementName);
			createdElement.setX(x);
			createdElement.setY(y);
			elements.add(createdElement);
			return createdElement;
		}
	}

	public PlaceElementInGameUseCase() {
		mockPlayEngine = new MockPlayEngine();
	}

	/**
	 * Method for placing element of the given name at the given position
	 * @param elementName name of element type to be placed, matching name given during authoring
	 * @param x xCoordinate to place it, from (grid corresponding to) mouse click
	 * @param y yCoordinate to place it, from (grid corresponding to) mouse click
	 */
	private void placeElement(String elementName, double x, double y) {
		TowerDefenseElement newlyPlacedElement = mockPlayEngine.placeElement(elementName, x, y);
		// Subsequent call to newlyPlacedElement.getCurrentElements() in game loop will
		// include this
		// DO SOME FRONT END STUFF TO MAKE THE ELEMENT STICK / APPEAR ON THE MAP
	}

}
