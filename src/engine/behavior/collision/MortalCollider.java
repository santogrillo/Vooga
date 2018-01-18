package engine.behavior.collision;

import engine.game_elements.ElementProperty;

/**
 * Represents mortal collider behavior - takes damage or explodes upon collision
 * with damage-dealing objects
 * Can be used by towers, soldiers, etc
 * @author radithya
 *
 */
public class MortalCollider extends GenericCollider {

	private double healthPoints;

	public MortalCollider(@ElementProperty(value = "playerId", isTemplateProperty = true) int playerId,
		                  @ElementProperty(value = "healthPoints", isTemplateProperty = true) double healthPoints,
		                  @ElementProperty(value = "explosionTemplate", isTemplateProperty = true) String explosionTemplate) {
		super(playerId,explosionTemplate);
		this.healthPoints = healthPoints;
	}

	@Override
	public void visit(DamageDealingCollisionVisitable visitable) {
		setHealthPoints(getHealthPoints() - visitable.getDamageToDeal());
	}

	@Override
	public boolean isAlive() {
		return healthPoints > 0;
	}

	protected double getHealthPoints() {
		return healthPoints;
	}

	private void setHealthPoints(double newHitPoints) {
		healthPoints = newHitPoints;
	}

	@Override
	public String explode() {
		return "";
	}
}
