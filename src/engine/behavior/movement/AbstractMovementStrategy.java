package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

/**
 * Use abstract class instead of interface to encapsulate x and y coordinate
 * data
 *
 * @author radithya
 *
 */
public abstract class AbstractMovementStrategy implements MovementStrategy {

	private LocationProperty locationProperty;
	private double angle;

	public AbstractMovementStrategy(
			@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint) {
		DoubleProperty xCoordinate = new SimpleDoubleProperty(startPoint.getX());
		DoubleProperty yCoordinate = new SimpleDoubleProperty(startPoint.getY());
		locationProperty = new LocationProperty(xCoordinate, yCoordinate);
		angle = 0;
	}

	/**
	 * Moves an object and returns its new location
	 * 
	 * @return objects new location
	 * */
	public abstract Point2D move();

	/**
	 * How the movement handler will handle being blocked by another object 
	 * 
	 * @param New desired x coordinate
	 * */
	public void handleBlock(String blockMethodName) {
		move();
	}
	
	/**
	 * Get the current X coordinate
	 * 
	 * @return Current X coordinate
	 * */
	public double getCurrentX() {
		return locationProperty.getCurrentX();
	}

	/**
	 * Get the current Y coordinate
	 * 
	 * @return Current Y coordinate
	 * */
	public double getCurrentY() {
		return locationProperty.getCurrentY();
	}

	/**
	 * Get the X and Y values for tracking
	 * 
	 * @return Tracking point of this object
	 * */
	public LocationProperty getPositionForTracking() {
		return locationProperty;
	}

	/**
	 * Set the X coordinate 
	 * 
	 * @param New desired x coordinate
	 * */
	public void setX(double newX) {
		locationProperty.setX(newX);
	}

	/**
	 * Set the Y coordinate 
	 * 
	 * @param New desired y coordinate
	 * */
	public void setY(double newY) {
		locationProperty.setY(newY);
	}
	
	/**
	 * Returns if the target was reached.
	 * By default, will return false.
	 * 
	 * @return false since target is not reached
	 * */
	public boolean targetReached() {
		return false;
	}

	/**
	 * Returns if the object should be removed when it reached its target
	 * 
	 * @return True if target should be removed, false if it should not
	 * */
	public boolean removeUponCompletion() {
		return true;
	}
	
	/**
	 * Returns the angle this object is facing
	 * 
	 * @return facing angle
	 */
	public double getAngle() {
		return angle;
	}
	
	/**
	 * Sets the facing angle of this object based on the target x and y coordinates
	 * 
	 * @param y target y coordinate
	 * @param x target x coordinate
	 */
	public void setAngle(double y, double x) {
		this.angle=Math.toDegrees(Math.atan2(y, x));
	}
	
	/**
	 * Set the facing angle of this object
	 * 
	 * @param angle New desired angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	/**
	 * Get the current coordinates of the object
	 * 
	 * @return Current coordinates of object
	 * */
	protected Point2D getCurrentCoordinates() {
		return new Point2D(getCurrentX(), getCurrentY());
	}
}
