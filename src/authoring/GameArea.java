package authoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import engine.AuthoringModelController;
import util.path.Path;
import util.path.PathParser;
import engine.authoring_engine.AuthoringController;
import display.interfaces.CustomizeInterface;
import display.interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import networking.protocol.PlayerServer.SpriteUpdate;
import display.sprites.BackgroundObject;
import display.sprites.InteractiveObject;
import display.sprites.StaticObject;

/**
 * 
 * @author bwelton
 *
 */
public class GameArea extends Pane implements CustomizeInterface, Droppable {
	private static final String DEFAULT_BUNDLE_PATH = "authoring/resources/GameArea";
	private final String WIDTH = "Game_Area_Width";
	private final String HEIGHT = "Game_Area_Height";
	private final String COLOR = "Game_Area_Color";
	private final String ROW_PERCENTAGE = "Grid_Row_Percentage";
	private final String COL_PERCENTAGE = "Grid_Column_Percentage";

	private int width;
	private int height;
	private int rowPercentage;
	private int colPercentage;
	private String backgroundColor;

	private AuthoringModelController myController;
	private ResourceBundle gameProperties;
	private PlacementGrid grid;
	private Path currentPath;
	private Map<Path, Color> paths;
	private PathParser parser;
	private boolean gridEnabled;
	private boolean moveableEnabled;

	private Group frontObjects;
	private Group pathObjects;
	private Group backObjects;
	private List<InteractiveObject> objectList;

	public GameArea(AuthoringModelController controller) {
		initializeProperties();
		initializeLayout();
		initializeHandlers();
		myController = controller;
		objectList = new ArrayList<>();
		frontObjects = new Group();
		pathObjects = new Group();
		backObjects = new Group();
		paths = new HashMap<>();
		currentPath = new Path(this);
		parser = new PathParser();
		grid = new PlacementGrid(this, width, height, rowPercentage, colPercentage, currentPath);

		this.getChildren().add(grid);
		this.getChildren().add(backObjects);
		this.getChildren().add(pathObjects);
		this.getChildren().add(frontObjects);
		paths.put(currentPath, currentPath.getColor());
		pathObjects.getChildren().add(currentPath);

		toggleGridVisibility(true);
		toggleMovement(false);
	}

	private void initializeProperties() {
		gameProperties = ResourceBundle.getBundle(DEFAULT_BUNDLE_PATH);
		width = Integer.parseInt(gameProperties.getString(WIDTH));
		height = Integer.parseInt(gameProperties.getString(HEIGHT));
		backgroundColor = gameProperties.getString(COLOR);
		rowPercentage = Integer.parseInt(gameProperties.getString(ROW_PERCENTAGE));
		colPercentage = Integer.parseInt(gameProperties.getString(COL_PERCENTAGE));
	}

	private void initializeLayout() {
		this.setPrefSize(width, height);
		this.setStyle("-fx-background-color: " + backgroundColor + ";");
	}

	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> gameAreaClicked(e, e.getX(), e.getY()));
	}

	protected void gameAreaClicked(MouseEvent e, double x, double y) {
		if (!currentPath.addWaypoint(e, x, y)) {
			Path newPath = new Path(this);
			grid.setActivePath(newPath);
			this.addAndSetActivePath(newPath);
			currentPath.addWaypoint(e, x, y);
		}
	}

	// For potential future extension for objects that cover paths
	public void addFrontObject(StaticObject object) {
		frontObjects.getChildren().add(object);
		objectList.add(object);
		object.setLocked(!moveableEnabled);
	}

	public void addBackObject(InteractiveObject newObject) {
		backObjects.getChildren().add(newObject);
		objectList.add(newObject);
		newObject.setLocked(!moveableEnabled);
	}

	protected void toggleGridVisibility(boolean visible) {
		grid.setVisible(visible);
		gridEnabled = visible;
	}

	protected void toggleMovement(boolean moveable) {
		moveableEnabled = moveable;
		if (moveable) {
			grid.toBack();
		} else {
			backObjects.toBack();
		}
		for (InteractiveObject s : objectList) {
			s.setLocked(!moveable);
		}
	}

	protected void resizeGameArea(int width, int height) {
		this.width = width;
		this.height = height;
		grid.resizeGrid(width, height);
		this.setPrefSize(width, height);
	}

	protected void savePath() {
		// parser.parse(path);
		// Method to save path to controller
	}

	@Override
	public void changeColor(String hexcode) {
		this.setStyle("-fx-background-color: " + hexcode + ";");
		backgroundColor = hexcode;
	}

	@Override
	public void droppedInto(InteractiveObject interactive) {
		if (gridEnabled) {
			Point2D newLocation = grid.place(interactive);
			interactive.setX(newLocation.getX());
			interactive.setY(newLocation.getY());
			if (frontObjects.getChildren().contains(interactive))
				return;
			for (Node node : objectList) {
				if (!(node instanceof BackgroundObject))
					node.toFront();
			}
		}
		SpriteUpdate interactiveUpdate = myController.moveElement(interactive.getElementId(), interactive.getX(),
				interactive.getY());
		interactive.setX(interactiveUpdate.getNewX());
		interactive.setY(interactiveUpdate.getNewY());
	}

	@Override
	public void objectRemoved(InteractiveObject interactive) {
		frontObjects.getChildren().remove(interactive);
		backObjects.getChildren().remove(interactive);
		objectList.remove(interactive);
		myController.deleteElement(interactive.getElementId());
	}

	@Override
	public void freeFromDroppable(InteractiveObject interactive) {
		grid.removeFromGrid(interactive);
	}

	@Override
	public Map<Path, Color> getPaths() {
		return paths;
	}

	protected void addAndSetActivePath(Path newPath) {
		pathObjects.getChildren().add(newPath);
		paths.put(newPath, newPath.getColor());
		currentPath = newPath;
	}

	public void updateActivePath(Path newActive) {
		if (newActive != currentPath)
			currentPath.deactivate();
		currentPath = newActive;
	}

	public void returnButtonPressed() {
		// TODO Auto-generated method stub
	}
}
