package engine.behavior.collision;

import engine.game_elements.ElementProperty;

public class ExplosiveCollider extends VolatileCollider {

	private boolean collided;
	
	public ExplosiveCollider(@ElementProperty(value = "playerId", isTemplateProperty = true) int playerId,
							 @ElementProperty(value = "explosionTemplate", isTemplateProperty = true) String explosionTemplate){
		super(playerId,explosionTemplate);
		collided = false;
	}

	@Override
	public boolean isAlive() {
		return !collided;
	}
	
	protected void handleCollision() {
		collided = true;
	}
}
