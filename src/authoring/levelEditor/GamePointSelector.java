package authoring.levelEditor;

import java.util.ArrayList;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import factory.AlertFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.PropertiesGetter;

public class GamePointSelector extends VBox{
	private final int POINT_DEFAULT = 100;
	private final String POINT_SELECTOR_PROMPT_TEXT = "pointPrompt";
	private final String UPDATE_TEXT = "updateButton";
	private final String DONE_LABEL = "doneLabel";
	private final String POINT_ALERT_MESSAGE = "invalidInputInfo";
	private final String POINT_ALERT_HEADER = "invalidInput";
	
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringModelController myController;
	private TextField amount;
	private Button update;
	private Button done;
	
	public GamePointSelector(AuthoringModelController controller) {
		myController = controller;		
		amount = new TextField();
		amount.setPromptText(PropertiesGetter.getProperty(POINT_SELECTOR_PROMPT_TEXT));
		update = new Button();
		update.setText(PropertiesGetter.getProperty(UPDATE_TEXT));
		update.setOnAction(e ->record(amount));
		done = new Button(PropertiesGetter.getProperty(DONE_LABEL));
		done.setOnAction(e->hide());
		this.getChildren().add(done);
		hide();

	}
	
	
	void show() {
		setVisible(true);
	}
	
	private void hide() {
		setVisible(false);
	}
	
	private void record(TextField amount) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int points;
		int currLv = myController.getCurrentLevel();
		for (int i = 0; i<checkBoxes.size(); i++) {
			if(checkBoxes.get(i).isSelected()) {
				selectedLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
				checkBoxes.get(i).fire();
			}
			else {
				myController.setLevel(Integer.parseInt(checkBoxes.get(i).getText()));
				myController.setLevelPointQuota(POINT_DEFAULT);
			}
		}
		try {
		points = Integer.parseInt(amount.getText());		
		}catch(NumberFormatException nfe) {
			new AlertFactory(POINT_ALERT_MESSAGE,POINT_ALERT_HEADER,AlertType.ERROR);
			points = POINT_DEFAULT;
		}
		
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelPointQuota(points);
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
