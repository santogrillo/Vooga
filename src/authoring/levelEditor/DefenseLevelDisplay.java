package authoring.levelEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import authoring.LevelToolBar.LevelTab;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

@Deprecated
public class DefenseLevelDisplay extends LevelDisplay {

	private Button newWave;
	private ResourceBundle myResources;
	private Map<String, List<String>> defaults;
	private List<ComboBox> myDropDowns;
	private static String DEFAULT_IMAGE_URL = "https://users.cs.duke.edu/~rcd/images/rcd.jpg\"";

	public DefenseLevelDisplay(int n, LevelTab lv, AuthoringModelController myController) {
		super(n, lv, myController);
		defaults = myController.getElementBaseConfigurationOptions();
		myDropDowns = new ArrayList<ComboBox>();
		for (String s : myController.getElementBaseConfigurationOptions().keySet()) {
		}
		// this would have to get refactored out depending on different languages and
		// all that.
		// TODO
		myResources = ResourceBundle.getBundle("authoring/resources/DefenseLevel"); // ideally this path would be to a
																					// valid resource bundle.
		createScene();

	}

	private void createScene() {
		newWave = new Button("Create new wave.");
		newWave.setOnAction(e -> createNewWave());
		newWave.setLayoutX(400);
		super.getLevelPane().getChildren().add(newWave);
	}

	private void createNewWave() {
		super.getLevelPane().getChildren().clear();
		TextField order = new TextField();
		order.setPromptText("Troops in this wave, separated by commas.");
		TextField start = new TextField();
		start.setPromptText("Starting point of this wave");
		TextField frequency = new TextField();
		frequency.setPromptText("How often do you want this wave to spawn?");
		TextField number = new TextField();
		number.setPromptText("How many times do you want this wave to spawn?");
		Button addWave = new Button("Add this wave!");
		/*
		 * for(String s: defaults.keySet()) { ComboBox x = new ComboBox();
		 * x.setPromptText(s); x.getItems().addAll(defaults.get(s)); myDropDowns.add(x);
		 * super.getLevelPane().getChildren().add(x); }
		 */
		/*
		 * There are some other properties over here, I'm sure, that I need to care
		 * about, but I'm not sure what they are.
		 */
		addWave.setOnAction(e -> {
			Map<String, String> fun = new HashMap<>();
			fun.put("frequency", frequency.getText());
			fun.put("number", number.getText());
			/*
			 * for (int i=0; i<defaults.size(); i++) { fun.put((String)
			 * defaults.keySet().toArray()[i], (String) defaults.entrySet().toArray()[i]); }
			 */
			fun.put("Collision effects", "Invulnerable to collision damage");
			fun.put("Collided-with effects", "Do nothing to colliding objects");
			fun.put("Move an object", "Object will stay at desired location");
			fun.put("Firing Behavior", "Shoot periodically");
			fun.put("imageUrl", DEFAULT_IMAGE_URL);
			fun.put("imageWidth", "45.0");
			fun.put("imageHeight", "45.0");
			fun.put("Numerical \"team\" association", "0");
			fun.put("Health points", "50");
			fun.put("Damage dealt to colliding objects", "20");
			fun.put("Speed of movement", "5");
			fun.put("initialAngle", "0");
			fun.put("radius", "10");
			fun.put("centerY", "0");
			fun.put("centerX", "0");
			fun.put("Target y-coordinate", "0");
			fun.put("Target x-coordinate", "0");
			fun.put("Projectile Type Name", Arrays.asList(order.getText().split(",")).get(0));
			fun.put("Attack period", "10");
//			super.getAuthor().setWaveProperties(fun, Arrays.asList(order.getText().split(",")),
//					new Point2D(Double.parseDouble(start.getText().split(",")[0]),
//							Double.parseDouble(start.getText().split(",")[1])));

		});
//		super.getLevelPane().getChildren().addAll(order, start, frequency, number, addWave);

	}

}
