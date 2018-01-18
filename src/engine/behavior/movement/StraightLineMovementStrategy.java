package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement strategy for objects that movements depends on straight line
 * movement
 * 
 * @author mscruggs
 *
 */

public class StraightLineMovementStrategy extends TargetedMovementStrategy {

	public StraightLineMovementStrategy(
			@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
			@ElementProperty(value = "targetX", isTemplateProperty = true) double targetX,
			@ElementProperty(value = "targetY", isTemplateProperty = true) double targetY,
			@ElementProperty(value = "velocity", isTemplateProperty = true) double velocity) {
		super(startPoint, targetX, targetY, velocity);
		setVelocityComponents();
	}
}
