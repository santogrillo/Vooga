package player;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBackground extends Rectangle {
	
	private final int HEALTH_HEIGHT = 40;
	private final int HEALTH_WIDTH = 200;
	private Rectangle healthBackground;
	private Rectangle healthBar;
	
	
	public HealthBackground() {
		this.setHeight(HEALTH_HEIGHT);
		this.setWidth(HEALTH_WIDTH);
		this.setFill(Color.RED);
	}

}
