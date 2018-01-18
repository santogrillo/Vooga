package authoring.levelEditor;

import java.util.ArrayList;

import display.splashScreen.ScreenDisplay;
import engine.AuthoringModelController;
import factory.AlertFactory;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import util.PropertiesGetter;

public class GameTimeSelector  extends VBox{
	private static final String TIME_SELECTOR_PROMPT_TEXT = "timeLimitPrompt";
	private static final String UPDATE_TEXT = "updateButton";
	private static final String DONE_LABEL = "doneLabel";
	private static final String TIME_ALERT_MESSAGE = "invalidInputInfo";
	private final String TIME_ALERT_HEADER = "invalidInput";
	private static final int TIME_DEFAULT = 600;
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringModelController myController;
	private TextField amount;
	private Button update;
	private Button done;

	public GameTimeSelector(AuthoringModelController controller) {
		myController = controller;		
		amount = new TextField();
		amount.setPromptText(PropertiesGetter.getProperty(TIME_SELECTOR_PROMPT_TEXT));
		update = new Button();
		update.setText(PropertiesGetter.getProperty(UPDATE_TEXT));
		update.setOnAction(e ->record(amount));
		done = new Button(PropertiesGetter.getProperty(DONE_LABEL));
		done.setOnAction(e->hide());
		this.getChildren().add(done);
		hide();
	}

	private void hide() {
		setVisible(false);
	}
	
	void show() {
		setVisible(true);
	}

	private void record(TextField amount) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int time;
		int currLv = myController.getCurrentLevel();
		for (int i = 0; i<checkBoxes.size(); i++) {
			if(checkBoxes.get(i).isSelected()) {
				selectedLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
				checkBoxes.get(i).fire();
			}
			else {
				myController.setLevel(Integer.parseInt(checkBoxes.get(i).getText()));
				myController.setLevelTimeLimit(TIME_DEFAULT);
			}
		}
		try {
		time = Integer.parseInt(amount.getText());		
		}catch(NumberFormatException nfe) {
			new AlertFactory(PropertiesGetter.getProperty(TIME_ALERT_MESSAGE),PropertiesGetter.getProperty(TIME_ALERT_HEADER),AlertType.ERROR);
			time = TIME_DEFAULT;
		}
		
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelTimeLimit(time* (int) ScreenDisplay.FRAMES_PER_SECOND);;
		}
		amount.clear();
		myController.setLevel(currLv);
	}
	
	public void createCheckBoxes(ArrayList<Integer> lvs) {
		checkBoxes = new ArrayList<>();
		for (Integer i : lvs) {
			CheckBox c =  new CheckBox();
			c.setAllowIndeterminate(false);
			c.setText(Integer.toString(i));
			checkBoxes.add(c);
		}
		this.getChildren().clear();
		this.getChildren().addAll(checkBoxes);
		this.getChildren().addAll(amount, update, done);
		
	}

}
