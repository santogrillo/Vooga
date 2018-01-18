package player;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * 
 * @author bwelton
 *
 */
public class HUD extends HBox{
	private Map<String, ValueDisplay> displays;
	private PointsDisplay myPointsDisplay;
	private HealthDisplay myHealthDisplay;
	private final double INITIAL_POINTS = 0;
	
	public HUD(int width) {
		this.setLayoutX(0);
		this.setLayoutY(0);
		this.setPrefWidth(width);
		
		Region leftFiller = new Region();
		this.getChildren().add(leftFiller);
		HBox.setHgrow(leftFiller, Priority.ALWAYS);
		
		addHealthDisplay();
		addPointsDisplay();
		
		displays = new HashMap<>();
	}

	public void update(Map<String, Double> values, int health) {
		for(String value:values.keySet()) {
			if(displays.containsKey(value)) {
				displays.get(value).setValue(values.get(value));
			}
		}
		myHealthDisplay.setValue(health);
	}
	
	public void initialize(Map<String, Double> values) {
		clearDisplays();
		
		for(String resource : values.keySet()) {
			addResourceDisplay(resource, values.get(resource));
		}
	}

	private void clearDisplays() {
		for(String display:displays.keySet()) {
			this.getChildren().remove(displays.get(display));
			displays.remove(display);
		}
	}

	private void addHealthDisplay() {
		myHealthDisplay = new HealthDisplay();
		this.getChildren().add(myHealthDisplay);
	}

	private void addPointsDisplay() {
		myPointsDisplay = new PointsDisplay();
		this.getChildren().add(myPointsDisplay);
	}
	
	protected void updatePointDisplay(double pointsEarned) {
		//this can be improved to associate point multipliers with certain resource types
		myPointsDisplay.increaseByAmount(pointsEarned);
	}

	private void addResourceDisplay(String resource, Double amount) {
		ValueDisplay display = new ValueDisplay();
		displays.put(resource, display);
		display.setStandardDisplayLabel(resource);
		display.setValue(amount);
		display.addItemsWithLabel();
		this.getChildren().add(display);
	}
	
	protected void resourcesEarned(Map<String, Double> resources) {
		for(String resource:resources.keySet()) {
			if(displays.containsKey(resource)) {
				displays.get(resource).increaseByAmount(resources.get(resource));
			}else {
				addResourceDisplay(resource, resources.get(resource));
			}
		}
	}
	
	protected boolean hasSufficientFunds(Map<String, Double> costs) {
		for(String cost : costs.keySet()) {
			if(!displays.containsKey(cost) || costs.get(cost) >= displays.get(cost).getQuantity()) {
				return false;
			}
		}
		return true;
	}

}
