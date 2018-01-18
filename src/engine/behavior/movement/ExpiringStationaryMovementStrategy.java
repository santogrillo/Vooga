package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;


/**
 * Movement strategy for object that will delete itself after a given amount of time
 * 
 * @author mscruggs
 *
 */
public class ExpiringStationaryMovementStrategy extends StationaryMovementStrategy{
	private int expirationTime;
	private int expirationTimer;
	private final double FPS = 60;
	public ExpiringStationaryMovementStrategy(@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
	@ElementProperty(value = "expirationTime", isTemplateProperty = true) int expirationTime) {
		super(startPoint);
		this.expirationTime = expirationTime;
		this.expirationTimer = 0;
	}
	
	public boolean targetReached() {
		return (expirationTimer >= expirationTime*FPS);
	}
	
	public Point2D move() {
		updateTimer();
		return getCurrentCoordinates();
	}
	
	private void updateTimer() {
		expirationTimer++;
	 }
}
