package engine.behavior.collision;

import engine.game_elements.ElementProperty;

/**
 * Captures general collision behaviors of most colliders. Specific collision
 * behaviors can be achieved by implementing visit(CollisionVisitable visitable)
 * for missing types and overriding where necessary
 * 
 * @author radithya
 *
 */
public abstract class GenericCollider implements CollisionVisitor {

	private int playerId;
	private boolean blocked;
	private String explosionTemplate;

	public GenericCollider(
			@ElementProperty(value = "playerId", isTemplateProperty = true) int playerId,
			@ElementProperty(value = "explosionTemplate", isTemplateProperty = true) String explosionTemplate) {
		this.playerId = playerId;
		this.explosionTemplate = explosionTemplate;
	}
	
	// All colliders (except perhaps an 'Unstoppable' collider type) would be
	// blocked by collision with an obstacle
	@Override
	public void visit(ImperviousCollisionVisitable visitable) {
		setBlocked();
	}

	// nothing happens when someone collides with a NoopCollisionVisitable
	@Override
	public void visit(NoopCollisionVisitable visitable) {
	}

	@Override
	public boolean isBlocked() {
		return blocked;
	}

	@Override
	public void unBlock() {
		blocked = false;
	}

	@Override
	public boolean isEnemy(CollisionVisitor other) {
		return getPlayerId() != other.getPlayerId();
	}
	
	@Override
	public int getPlayerId() {
		return playerId;
	}
	
	private void setBlocked() {
		blocked = true;
	}
	
	public String explode() {
		return explosionTemplate;
	}

}
