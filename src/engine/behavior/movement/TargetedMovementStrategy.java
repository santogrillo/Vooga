package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement Strategy that moves towards a targeted location
 * 
 * 
 * @author mscruggs
 */
public class TargetedMovementStrategy extends AbstractMovementStrategy {

    private double targetX;
    private double targetY;
    private double xVelocity;
    private double yVelocity;
    private double velocityMagnitude;

    protected TargetedMovementStrategy(
            @ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
            @ElementProperty(value = "targetX", isTemplateProperty = true) double targetX,
            @ElementProperty(value = "targetY", isTemplateProperty = true) double targetY,
            @ElementProperty(value = "velocityMagnitude", isTemplateProperty = true) double velocityMagnitude) {
        super(startPoint);
        setTargetCoordinates(targetX, targetY);
        this.velocityMagnitude = velocityMagnitude;
    }

    /**
     * Move the object in straight line towards a targeted direction
     * 
     * @return the updated coordinates of the object
     * */
    public Point2D move() {
    	setVelocityComponents();
    	setAngle(yVelocity,xVelocity);
    	if(targetReached() && removeUponCompletion()) {
    		setX(this.getTargetX());
    		setY(this.getTargetY());
    	}
    	else{
    		setX(this.getCurrentX()+ getXVelocity());
        	setY(this.getCurrentY()+ getYVelocity());
    	}
    	return getCurrentCoordinates();
    }
    
    /**
     * Stop the object from moving
     * 
     * */
    public void stop() {
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.velocityMagnitude = 0;
    }
    
    /**
     * Returns if the target has reached its destination
     * 
     * @return True if target is within one movement of its location, false otherwise
     * */
    public boolean targetReached() {
    	return (getDistance()<velocityMagnitude);
    }

    /**
     * Sets the target coordinates of the object to move towards
     * */
    protected void setTargetCoordinates(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    protected double getTargetX() {
        return targetX;
    }

    protected double getTargetY() {
        return targetY;
    }

    protected double getXVelocity() {
        return xVelocity;
    }

    protected double getYVelocity() {
        return yVelocity;
    }
    
    protected void setVelocityMagnitude(double veloMagnitude) {
    	this.velocityMagnitude = veloMagnitude;
    }

    protected void setXVelocity(double newXVelocity) {
        xVelocity = newXVelocity;
    }

    protected void setYVelocity(double newYVelocity) {
        yVelocity = newYVelocity;
    }

    /**
     * Sets the velocity components given nothing.
     * Uses the current location and the target location
     * 
     * */
    protected void setVelocityComponents() {
        setXVelocity(getVelocityComponent(getTargetX()-getCurrentX()));  
        setYVelocity(getVelocityComponent(getTargetY()-getCurrentY()));
    }
    
    /**
     * Sets the velocity components given an angle.
     * Sets components based on passed angle.
     * 
     * @param angle Angle to calculate velocity components
     * */
    protected void setVelocityComponents(double angle) {
    	setAngle(angle);
    	setXVelocity(velocityMagnitude * Math.cos(Math.toRadians(angle)));
        setYVelocity(velocityMagnitude * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Get the distance between the current location and the targeted location
     * 
     * @return the distance between the target and the current location
     * */
    private double getDistance() {
    	return Math.hypot(this.getTargetX()-this.getCurrentX(), this.getTargetY()-this.getCurrentY());
    }
    
    /**
     * Get the velocity component based on the component distance
     * 
     * @return the velocity component of passed distance
     * */
    private double getVelocityComponent(double componentDistance) {
    	if(getDistance() == 0.0) {
    		return 0.0;
    	}
    	return componentDistance*velocityMagnitude/getDistance();
    }
}
