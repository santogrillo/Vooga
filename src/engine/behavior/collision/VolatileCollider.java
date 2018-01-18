package engine.behavior.collision;

import engine.game_elements.ElementProperty;

/**
 * Represents volatile 'explosion' behavior - simply explodes / self-destructs
 * upon colliding with (any?) other element
 * Can be used by projectiles
 * @author radithya
 *
 */
public abstract class VolatileCollider extends GenericCollider {
	// Can be sub-classed by different kinds of projectiles
	public VolatileCollider(@ElementProperty(value = "playerId", isTemplateProperty = true) int playerId,
			@ElementProperty(value = "explosionTemplate", isTemplateProperty = true) String explosionTemplate) {
		super(playerId,explosionTemplate);
	}

	@Override
	public void visit(ImperviousCollisionVisitable visitable) {
		handleCollision();
	}

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		handleCollision();
	}

	@Override
	public void visit(NoopCollisionVisitable visitable) {
		handleCollision();
	}
	
	protected abstract void handleCollision();

	

}
