package authoring.levelEditor;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.layout.VBox;

/**
 * 
 * @author venkat
 *
 */
public class GameEnder extends VBox{
	private AuthoringModelController myController;
	private GameEnderConditions conditions;
	private GameHealthSelector health;
	private GamePointSelector points;
	private GameTimeSelector time;
	
	
	public GameEnder(AuthoringModelController controller) {
		myController = controller; 
		conditions = new GameEnderConditions(controller);
		health = new GameHealthSelector(controller);
		points = new GamePointSelector(controller);
		time = new GameTimeSelector(controller);
		this.getChildren().addAll(conditions, health, points, time);
		this.setPrefWidth(300);
		this.setSpacing(100);
		}
	
	public void update() {
		conditions.update();
	}
	
	public void setRecorder(GameEnderRecorder r) {
		conditions.setRecorder(r);
		conditions.setTimeRecorder(time);
		conditions.setPointRecorder(points);
	}
	
}
