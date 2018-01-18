package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement strategy for object that will move in a straight line in a given direction
 * 
 * @author mscruggs
 *
 */
public class DirectionalMovementStrategy extends TargetedMovementStrategy{

	private double angle;
	private final double DEFAULT_STARTING_POSITION = -1;
	
	public DirectionalMovementStrategy(
			@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
			@ElementProperty(value = "velocityMagnitude", isTemplateProperty = true) double velocityMagnitude,
			@ElementProperty(value = "angle", isTemplateProperty = true) double angle) {
		super(startPoint, -1,-1, velocityMagnitude);
		this.angle = Math.toRadians(angle);
		setVelocityComponents(this.angle);
	}
	
	public Point2D move() {
		setVelocityComponents(angle);
		setX(getCurrentX() + getXVelocity());
		setY(getCurrentY() + getYVelocity());
		return getCurrentCoordinates();
	}
	
	public boolean targetReached() {
		return false;
	}
	
	public boolean removeUponCompletion() {
		return false;
	}
	
	public void setAngle(double angle) {
		this.angle = Math.toRadians(angle);
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void incrementAngle(double angleIncrement) {
		this.angle += Math.toRadians(angleIncrement);
	}
	
}
