package player;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends Rectangle {
	
	private Rectangle healthBackground;
	private Rectangle healthBar;
	private double myHealth;
	private final int INITIAL_HEALTH = 200;
	private final int HEALTH_HEIGHT = 40;
	private final int HEALTH_WIDTH = 200;
	
	public HealthBar() {
		myHealth = INITIAL_HEALTH;
		this.setHeight(HEALTH_HEIGHT);
		this.setWidth(HEALTH_WIDTH);
		this.setFill(Color.LIGHTGREEN);
	}

	public void decreaseHealth(double amount) {
		myHealth -= amount;
		this.setWidth(myHealth);
		
	}

}
