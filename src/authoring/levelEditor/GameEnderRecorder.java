package authoring.levelEditor;

import java.util.Collection;
import java.util.Map;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import util.PropertiesGetter;

/**
 * 
 * @author venkat
 *
 */
public class GameEnderRecorder extends VBox{
	private TableView <Conditions> victoryConditions;
	private TableView<Conditions> defeatConditions;
	private AuthoringModelController myController;
	private final String VICTORY_TEXT = "victoryText";
	private final String DEFEAT_TEXT = "defeatText";
	private final String CONDITIONS_TEXT = "conditionText";
	private final String MY_CONDITIONS_TEXT = "myConditionText";
	private final String LEVELS_TEXT = "levelText";
	private final String MY_LEVELS_TEXT = "myLevelText";
	private final int COLUMN_PREF_WIDTH = 120;

	
	public GameEnderRecorder(AuthoringModelController controller) {
//		myCompletedLevels=levels;
		myController = controller;
		setUp();
		
	}

	private void setUp() {
		this.getChildren().clear();
		makeTables();
		update();
		
	}

	
	public void update() {
		victoryConditions.getItems().clear();
		defeatConditions.getItems().clear();
		Map<String, Collection<Integer>> victory = myController.getCurrentVictoryConditions();
		Map<String, Collection<Integer>> defeat = myController.getCurrentDefeatConditions();
		for (String s : victory.keySet()) {
			System.out.println(s + " " + victory.get(s));
		}
		makeConditions(victory, victoryConditions.getItems());
		makeConditions(defeat, defeatConditions.getItems());
		
	}
	
	
	private void makeTables() {
		ObservableList<Conditions> vicConditions = FXCollections.observableArrayList();
		ObservableList<Conditions> lossConditions = FXCollections.observableArrayList();
		Map<String, Collection<Integer>> victory = myController.getCurrentVictoryConditions();
		Map<String, Collection<Integer>> defeat = myController.getCurrentDefeatConditions();
		makeConditions(victory, vicConditions);
		makeConditions(defeat, lossConditions);
		victoryConditions = new TableView<Conditions>();
		defeatConditions = new TableView<Conditions>();
		fillTable(PropertiesGetter.getProperty(VICTORY_TEXT), victoryConditions, vicConditions);
		fillTable(PropertiesGetter.getProperty(DEFEAT_TEXT), defeatConditions, lossConditions);
		this.getChildren().addAll(victoryConditions, defeatConditions);
		
	}

	private void fillTable(String cond, TableView<Conditions> table, ObservableList<Conditions> conditions) {
		TableColumn<Conditions, String> condColumn = makeColumn(cond + PropertiesGetter.getProperty(CONDITIONS_TEXT), PropertiesGetter.getProperty(MY_CONDITIONS_TEXT));
		TableColumn<Conditions, String> levelsColumn = makeColumn(PropertiesGetter.getProperty(LEVELS_TEXT), PropertiesGetter.getProperty(MY_LEVELS_TEXT));
		table.setItems(conditions);
		table.getColumns().addAll(condColumn, levelsColumn);
		
	}

	private TableColumn<Conditions, String> makeColumn(String title, String instanceVar) {
		TableColumn<Conditions, String> column = new TableColumn<>(title);
		column.setPrefWidth(COLUMN_PREF_WIDTH);
		column.setCellValueFactory(new PropertyValueFactory<>(instanceVar));
		return column;
	}

	private void makeConditions(Map<String, Collection<Integer>> list, ObservableList<Conditions> conditions) {
		for(String s : list.keySet()) {
			Conditions c = new Conditions(s, list.get(s));
			conditions.add(c);
		}
	}
}
