package authoring.LevelToolBar;

import java.util.Set;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class LevelTab extends ScrollPane{
	
	//could/SHOULD be refactored to just hold the tab it's a part of?
	private final int DISPLAY_SIZE = 40;
	private int myNumber;
	private Button editLevel;
//	private LevelDisplay myLevelDisplay;
	private boolean edited;
	private AuthoringModelController myController;
	private HBox myDisplay;
	
	public LevelTab(int n, AuthoringModelController controller) {
		myNumber = n;
		myDisplay = new HBox();
		this.setContent(myDisplay);
		myController = controller;
		//needs to be a check for what kind of level it is, so that we either create an attackleveldisplay or a 
		//defenseleveldisplay
		
//		myLevelDisplay = new DefenseLevelDisplay(n, this, controller); //for now assuming it has to be a defense one.
//		Label initialLabel = new Label("You have to add content to this first! Click the edit button!");
//		updateLevelContents();
	}


//	public void updateLevelContents() {
//		Set<String> myContents = myController.getInventory();
//		
//		
//	}


//	public void openLevelDisplay() {
//		myLevelDisplay.open();
//	}
	
	public void attach(Tab level) {
		level.setContent(this);
	}

	public int getLvNumber() {
		return myNumber;
	}

	public void update(ImageView currSprite) {
		//TODO
		//this method will update the different components in this tab, for the display. 	
		//probably will involve using a lot of get methods to get stuff from the back end in order to display them.
		//waiting on a get method in back end.
		myDisplay.getChildren().add(currSprite);
		
	}


	public void decrementLevel() {
		myNumber-=1;
//		myLevelDisplay.decrementLevel();
		
	}
}
