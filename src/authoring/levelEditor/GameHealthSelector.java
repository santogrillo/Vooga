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
import javafx.scene.layout.VBox;
import util.PropertiesGetter;

public class GameHealthSelector extends VBox{
	private final int HEALTH_DEFAULT = 100;
	private final String HEALTH_HEADER_TEXT = "invalidInput";
	private final String HEALTH_PROMPT_TEXT = "healthAmount";
	private final String HEALTH_UPDATE_TEXT = "updateButton";
	private final String HEALTH_INVALID_INPUT_WARNING = "invalidInputInfo";
	private ArrayList<CheckBox> checkBoxes;
	private AuthoringModelController myController;

	public GameHealthSelector(AuthoringModelController controller) {
		myController = controller;
		
		TextField amount = new TextField();
		amount.setPromptText(PropertiesGetter.getProperty(HEALTH_PROMPT_TEXT));
		Button update = new Button();
		update.setText(PropertiesGetter.getProperty(HEALTH_UPDATE_TEXT));
		createLevelBoxes();
		update.setOnAction(e ->{
			record(amount, checkBoxes);});
		this.getChildren().add(amount);
		this.getChildren().add(update);
		
	}

	private void record(TextField amount, ArrayList<CheckBox> levels) {
		ArrayList<Integer> selectedLevels = new ArrayList<>();
		int health;
		for (int i = 0; i<levels.size(); i++) {
			if(levels.get(i).isSelected()) {
				selectedLevels.add(i+1);
			}
		}
		try {
		health = Integer.parseInt(amount.getText());		
		}catch(NumberFormatException nfe) {
			new AlertFactory(PropertiesGetter.getProperty(HEALTH_INVALID_INPUT_WARNING),PropertiesGetter.getProperty(HEALTH_HEADER_TEXT),AlertType.ERROR);
			health = HEALTH_DEFAULT;
		}
		int currLv = myController.getCurrentLevel();
		for (Integer i : selectedLevels) {
			myController.setLevel(i);
			myController.setLevelHealth(health);
		}
	}

	private void createLevelBoxes() {
		checkBoxes = new ArrayList<>();
		for (int i = 0; i<myController.getNumLevelsForGame(); i++) {
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i).setText(Integer.toString(i+1));
		}
		this.getChildren().addAll(checkBoxes);
	}
}
