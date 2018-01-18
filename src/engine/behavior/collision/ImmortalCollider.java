package engine.behavior.collision;

import engine.game_elements.ElementProperty;

/**
 * Represents immortal collider behavior - does not take any damage, always
 * alive
 * Could be used by immortal units - like in GodMode
 * @author radithya
 *
 */
public class ImmortalCollider extends GenericCollider {

	public ImmortalCollider(
			@ElementProperty(value = "playerId", isTemplateProperty = true) int playerId) {
		super(playerId,"");
	}
	
	// Immortal colliders don't take any damage
	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
	}

	@Override
	public boolean isAlive() {
		return true; // ImmortalColliders (Obstacles, etc) are always alive
	}

}
