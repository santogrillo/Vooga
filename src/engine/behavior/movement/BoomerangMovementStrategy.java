package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement strategy for object that will move in a elliptical path at a target
 * 
 * @author mscruggs
 *
 */
public class BoomerangMovementStrategy extends CircularMovementStrategy{

	private Point2D startingLocation;
	public BoomerangMovementStrategy(
			@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
			@ElementProperty(value = "targetX", isTemplateProperty = true) double targetX,
			@ElementProperty(value = "targetY", isTemplateProperty = true) double targetY,
			@ElementProperty(value = "velocity", isTemplateProperty = true) double velocity) {
		super(startPoint,
			  startPoint.distance(targetX, targetY)/2,
			  startPoint.angle(targetX,targetY),
			  velocity);
		startingLocation = startPoint;
	}
	
	public boolean targetReached() {
		return ((startingLocation.getX() == this.getTargetX()) &&
				(startingLocation.getY() == this.getTargetY()));
	}
	
}
