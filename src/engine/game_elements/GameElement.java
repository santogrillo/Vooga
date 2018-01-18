package engine.game_elements;

import java.util.List;

import engine.behavior.collision.CollisionHandler;
import engine.behavior.firing.FiringStrategy;
import engine.behavior.movement.MovementStrategy;
import engine.behavior.movement.LocationProperty;
import javafx.scene.image.ImageView;

import javafx.geometry.Point2D;

/**
 * Represents game objects in the backend. Responsible for controlling the
 * object's update behavior.
 *
 * TODO - documentation
 *
 * @author Ben Schwennesen
 */
public final class GameElement {

	public enum Team {
		NEUTRAL, HUMAN, COMPUTER
	}

	private FiringStrategy firingStrategy;
	private MovementStrategy movementStrategy;
	private CollisionHandler collisionHandler;

	private String templateName;

	public GameElement(String templateName, FiringStrategy firingStrategy, MovementStrategy movementStrategy,
					   CollisionHandler collisionHandler) {
		this.templateName = templateName;
		this.firingStrategy = firingStrategy;
		this.movementStrategy = movementStrategy;
		this.collisionHandler = collisionHandler;

	}

	/**
	 * Move one cycle in direction of current velocity vector
	 */
	public void move() {
		if (collisionHandler.isBlocked()) {
			// movementStrategy.handleBlock();
			// TODO - handle block
			collisionHandler.unBlock();
		}
		Point2D newLocation = movementStrategy.move();
		collisionHandler.getGraphicalRepresentation().setX(newLocation.getX());
		collisionHandler.getGraphicalRepresentation().setY(newLocation.getY());
	}

	public boolean shouldFire(double distanceToTarget) {
		return firingStrategy.shouldFire(distanceToTarget);
	}

	public String fire() {
		return firingStrategy.fire();
	}
	
	
	/**
	 * Check for a collision with another sprite.
	 *
	 * @param other
	 *            the other sprite with which this sprite might be colliding
	 * @return true if the sprites collide, false otherwise
	 */
	public boolean collidesWith(GameElement other) {
		return this.collisionHandler.collidesWith(other.collisionHandler);
	}

	/**
	 * Apply the effects of a collision with other sprites to this sprite.
	 * 
	 * @param otherElements
	 *            the other sprites with which this sprite collided with
	 */
	public void processCollision(List<GameElement> otherElements) {
		for(GameElement other : otherElements) {
			this.collisionHandler.processCollision(other.collisionHandler);
		}
	}

	/**
	 * Check if this sprite has been destroyed during gameplay.
	 *
	 * @return true if the sprite has not been destroyed, false otherwise
	 */
	public boolean isAlive() {
		return collisionHandler.isAlive() && !firingStrategy.isExpended()
				&& !(reachedTarget() && shouldRemoveUponCompletion());
	}

	public boolean reachedTarget() {
		return movementStrategy.targetReached();
	}

	/**
	 * Auto-updating (NOT snapshot) position of this AbstractMovementStrategy for
	 * tracking
	 *
	 * @return auto-updating position that changes with movement
	 */
	public LocationProperty getLocationProperty() {
		return movementStrategy.getPositionForTracking();
	}

	public double getX() {
		return movementStrategy.getCurrentX();
	}

	public double getY() {
		return movementStrategy.getCurrentY();
	}
	
	public double getAngle() {
		return movementStrategy.getAngle();
	}
	
	public String getFiringAudio() {
		return firingStrategy.getAudioUrl();
	}
	
	public String getCollisionAudio() {
		return collisionHandler.getAudioUrl();
	}

	public boolean shouldRemoveUponCompletion() {
		return movementStrategy.removeUponCompletion();
	}

	public ImageView getGraphicalRepresentation() {
		return collisionHandler.getGraphicalRepresentation();
	}
	
	public String getImageUrl() {
		return collisionHandler.getImageUrl();
	}

	public void setX(double newX) {
		collisionHandler.getGraphicalRepresentation().setX(newX);
		movementStrategy.setX(newX);
	}

	public void setY(double newY) {
		collisionHandler.getGraphicalRepresentation().setY(newY);
		movementStrategy.setY(newY);
	}

	/**
	 * Player id corresponding to player owning this sprite
	 * 
	 * @return id of player controlling this sprite
	 */
	public int getPlayerId() {
		return collisionHandler.getPlayerId();
	}

	// TODO (extension) - for multi-player extension, modify to take in a playerId
	// parameter
	public boolean isEnemy() {
		return getPlayerId() == Team.COMPUTER.ordinal();
	}

	// TODO (extension) - for multi-player extension, modify to take in a playerId
	// parameter
	public boolean isAlly() {
		return getPlayerId() == Team.HUMAN.ordinal();
	}
	

	public boolean shouldExplode() {
		return collisionHandler.shouldExplode();
	}
	
	public String explode() {
		return collisionHandler.explode();
	}

	public double getBlastRadius() {
		return collisionHandler.getBlastRadius();
	}

	public String getTemplateName() {
		return templateName;
	}
}
