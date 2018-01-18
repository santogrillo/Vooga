package player;

import networking.protocol.PlayerServer.NewSprite;
import util.path.Path;
import util.protocol.ClientMessageUtils;
import engine.PlayModelController;

import java.util.Map;
import java.util.ResourceBundle;

import display.interfaces.Droppable;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import display.sprites.InteractiveObject;

/**
 * 
 * @author bwelton, adi, ben s, sgrillo
 *
 */
public class PlayArea extends Pane implements Droppable {
	private final String WIDTH = "Game_Area_Width";
	private final String HEIGHT = "Game_Area_Height";
	private final String ROW_PERCENTAGE = "Grid_Row_Percentage";
	private final String COLOR = "Game_Area_Color";

	private ResourceBundle gameProperties;
	private PlayModelController myController;
	private ClientMessageUtils clientMessageUtils;

	private int width;
	private int height;
	private int rowPercentage;
	private String backgroundColor;
	private double lastX;
	private double lastY;

	public PlayArea(PlayModelController controller, ClientMessageUtils clientMessageUtils) {
		myController = controller;
		this.clientMessageUtils = clientMessageUtils;
		initializeProperties();
		initializeLayout();
	}

	private void initializeProperties() {
		gameProperties = ResourceBundle.getBundle("authoring/resources/GameArea");
		width = Integer.parseInt(gameProperties.getString(WIDTH));
		height = Integer.parseInt(gameProperties.getString(HEIGHT));
		backgroundColor = gameProperties.getString(COLOR);
	}

	private void initializeLayout() {
		this.setLayoutX(310);
		this.setLayoutY(10);
		this.setPrefHeight(width);
		this.setPrefWidth(height);
		this.getStylesheets().add("player/resources/playerPanes.css");
		this.getStyleClass().add("play-area");
		this.setStyle("-fx-background-color: " + backgroundColor + ";");
	}

	protected void placeInGrid(InteractiveObject currObject) {
		lastX = currObject.getX();
		lastY = currObject.getY();
		Point2D startLocation = new Point2D(currObject.getX(), currObject.getY());
		NewSprite newSprite = myController.placeElement(currObject.getElementName(), startLocation);
		clientMessageUtils.addNewSpriteToDisplay(newSprite);

	}

	@Override
	public void droppedInto(InteractiveObject interactive) {
		if (!interactive.intersects(this.getLayoutBounds())) {
			interactive.setX(lastX);
			interactive.setY(lastY);
		} else {
			placeInGrid(interactive);
		}
	}

	@Override
	public void objectRemoved(InteractiveObject interactive) {
		this.getChildren().remove(interactive);
	}

	@Override
	public void freeFromDroppable(InteractiveObject interactive) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Path, Color> getPaths() {
		return null;
	}

}
