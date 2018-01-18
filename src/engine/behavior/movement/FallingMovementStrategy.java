package engine.behavior.movement;

import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;

/**
 * Movement strategy for object that will fall at a given acceleration
 * 
 * @author mscruggs
 *
 */
public class FallingMovementStrategy extends AbstractMovementStrategy{
	private double gravity;
	private double yVelocity;
	private double xVelocity;
	private final double FPS = 60;
	
	
	public FallingMovementStrategy(@ElementProperty(value = "startPoint", isTemplateProperty = false) Point2D startPoint,
			@ElementProperty(value = "initialYVelocity", isTemplateProperty = true) double initialYVelocity, 
			@ElementProperty(value = "xVelocity", isTemplateProperty = true) double xVelocity,
			@ElementProperty(value = "gravity", isTemplateProperty = true) double gravity) {
		super(startPoint);
		this.gravity = gravity;
		yVelocity = initialYVelocity;
		this.xVelocity = xVelocity;
	}

	public Point2D move() {
		updateYVelocity();
		this.setX(getCurrentX()+xVelocity);
		this.setY(getCurrentY()+yVelocity);
		return this.getCurrentCoordinates();
	}
	
	protected void setYVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}
	
	private void updateYVelocity() {
		yVelocity -= (gravity/FPS);
	}
	
	public static void main(String[] args) throws InterruptedException {
		FallingMovementStrategy fall = new FallingMovementStrategy(new Point2D(0,0),0,0,10);
		for(int i=0;i<600;i++) {
			fall.move();
			System.out.println(fall.yVelocity);
		}
	}

}
