package authoring.levelEditor;

import java.util.ArrayList;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import util.PropertiesGetter;

/**
 * 
 * @author venkat
 *
 */
public class GameEnderConditions extends VBox {
	private static final String VICTORY_TIME_CONDITIONS = "timeLimit";
	private AuthoringModelController myController;
	private ComboBox<String> victory;
	private ComboBox<String> defeat;
	private ArrayList<CheckBox> checkBoxes;
	private GameEnderRecorder recorder;
	private GamePointSelector pointManager;
	private GameTimeSelector timeManager;
	private final String VICTORY_PROMPT_TEXT = "winQuestion";
	private final String DEFEAT_PROMPT_TEXT = "lossQuestion";
	private final String RECORD_CONDITIONS_LABEL = "endConditionRecorder";
	private final String LEVELS_CONDITIONS_PROMPT_TEXT = "endConditionSelector";
	private final String VICTORY_POINTS_CONDITIONS = "pointsLimit";
	private final boolean IS_WRAPPED = true;
	private final boolean IS_ALLOW_INDETERMINATE = false;

	public GameEnderConditions(AuthoringModelController controller) {
		myController = controller; 
		addMiscElements();
	}
	
	private void addMiscElements() {
		victory = new ComboBox<>();
		victory.setPromptText(PropertiesGetter.getProperty(VICTORY_PROMPT_TEXT));
		victory.getItems().addAll(myController.getPossibleVictoryConditions());
		defeat = new ComboBox<>();
		defeat.setPromptText(PropertiesGetter.getProperty(DEFEAT_PROMPT_TEXT));
		defeat.getItems().addAll(myController.getPossibleDefeatConditions());
		
		Button recordConditions = new Button(PropertiesGetter.getProperty(RECORD_CONDITIONS_LABEL));
		recordConditions.setOnAction(e->record());
		Label l = new Label(PropertiesGetter.getProperty(LEVELS_CONDITIONS_PROMPT_TEXT));
		l.setWrapText(IS_WRAPPED);
		this.getChildren().addAll(victory, defeat, l);
		createLevelBoxes();
		this.getChildren().add(recordConditions);
	}
	
	private void createLevelBoxes() {
		checkBoxes = new ArrayList<>();
		for (int i = 1; i<=myController.getNumLevelsForGame(); i++) {
			System.out.println(myController.getNumLevelsForGame());
			checkBoxes.add(new CheckBox());
			checkBoxes.get(i-1).setText(Integer.toString(i));
			checkBoxes.get(i-1).setAllowIndeterminate(IS_ALLOW_INDETERMINATE);
		}
		this.getChildren().addAll(checkBoxes);
		
	}

	private void record() {
		int currLevel = myController.getCurrentLevel();
		ArrayList<Integer> selectedPointLevels = new ArrayList<>();
		ArrayList<Integer> selectedTimeLevels = new ArrayList<>();
		for(int i = 0; i<myController.getNumLevelsForGame(); i++) {
			myController.setLevel(i);
			
			if (checkBoxes.get(i).isSelected()) {
				if(victory.getValue()!=null) {
					myController.setVictoryCondition(victory.getValue());
					if(victory.getValue().equals(PropertiesGetter.getProperty(VICTORY_POINTS_CONDITIONS))) {
						selectedPointLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
					}
					if(victory.getValue().equals(PropertiesGetter.getProperty(VICTORY_TIME_CONDITIONS))) {
						selectedTimeLevels.add(Integer.parseInt(checkBoxes.get(i).getText()));
					}
				}
				if(defeat.getValue()!=null) {
					myController.setDefeatCondition(defeat.getValue());	
				}
				
				checkBoxes.get(i).fire();
		}
	
	}
		if (selectedPointLevels.size()>0) {
			pointManager.createCheckBoxes(selectedPointLevels);    
			pointManager.show();
		}
		
		if (selectedTimeLevels.size()>0) {
			timeManager.createCheckBoxes(selectedTimeLevels);
			timeManager.show();
		}
		
		victory.setValue(null);
		defeat.setValue(null);
		myController.setLevel(currLevel);
		
		recorder.update();
	}
	

	public void update() {
		this.getChildren().removeAll(checkBoxes);
		createLevelBoxes();
	}

	public void setRecorder(GameEnderRecorder r) {
		recorder = r;
		
	}

	public void setPointRecorder(GamePointSelector points) {
		pointManager = points;
	}
	
	public void setTimeRecorder(GameTimeSelector time) {
		timeManager = time;
	}
}