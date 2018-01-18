package authoring.levelEditor;

import java.util.ResourceBundle;

import authoring.LevelToolBar.LevelTab;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * @author venkat
 * Window that acts as a display for each individual level. - Super Class
 * Allows editing of information about the level in terms of 
 *  *the number of waves/contents of each wave--for a defensive game.
 *  *the time limit of the game--for the offensive game.
 *  
 *  Honestly, it feels very possible for us to also have the scene and the pane in this class itself, but I'm unsure
 *  Let me know. Make this abstract, maybe?
 * 
 */
@Deprecated
public class LevelDisplay {
	private int myNumber;
	private Stage myStage;
	private LevelTab myLv;
	private AuthoringModelController myController;
	private ResourceDisplay resources;
	private ResourceBundle rb;
	private Scene myScene;
	private VBox centerPane;
	private GameEnder ender;
	private BorderPane myRoot;
	
	
	
	public LevelDisplay(int n, LevelTab lv, AuthoringModelController controller) {
		myRoot = new BorderPane();
		centerPane = new VBox();
		resources = new ResourceDisplay(controller);
//		ender = new GameEnder(controller);
		myRoot.setLeft(ender);
		myRoot.setRight(resources.getRoot());
		myRoot.setCenter(centerPane);
		myScene = new Scene(myRoot); 
		myRoot.setPrefWidth(700);
		myRoot.setPrefHeight(200);
		myNumber = n;
		myController = controller;
		myStage = new Stage();
//		myStage.setTitle(rb.getString("lvNum")+ " " + n);
		myStage.setTitle("Level Number " + n);
//		myStage.setOnCloseRequest(e->myLv.update());
		myStage.setScene(myScene);
	}
	
	public void open() {
		//should updating happen here or in the sub
		myStage.show();
	}

	protected Stage getStage() {
		return myStage;
	}
	
	protected int getLevelNumber() {
		return myNumber;
	}

	public void decrementLevel() {
		myNumber-=1;
	}

	public AuthoringModelController getAuthor() {
		// TODO Auto-generated method stub
		return myController;
	}
	
	
	protected VBox getLevelPane() {
		return centerPane;
	}
	
}
