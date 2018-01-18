package authoring.levelEditor;

import authoring.EditDisplay;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author venkat, moboyle
 *
 */
public class LevelsEditDisplay {
	private static final int SIZE = 800;
	
	private Stage myStage;
	private Scene myScene;
	private AuthoringModelController myController;
	private BorderPane myRoot;
	private ResourceDisplay resourceEditor;
	private GameEnder gameEnder;
	private GameEnderRecorder recorder;
	private final String LEVEL_EDITOR_TITLE = "Level Editor";
	
	public LevelsEditDisplay(AuthoringModelController controller) {
		myController = controller;
		myStage = new Stage();
		myStage.setTitle(LEVEL_EDITOR_TITLE);
		myRoot = new BorderPane();
		myScene = new Scene(myRoot, SIZE, SIZE);
		resourceEditor = new ResourceDisplay(controller);
		gameEnder = new GameEnder(controller);
		recorder = new GameEnderRecorder(controller);
		
		gameEnder.setRecorder(recorder);
		myRoot.setLeft(gameEnder);
		myRoot.setRight(resourceEditor);
		myRoot.setCenter(recorder);
		myStage.setScene(myScene);
	}
	
	
	public void open() {
		myStage.show();
		resourceEditor.updateCurrentState();
		gameEnder.update();
		recorder.update();
	}
}
