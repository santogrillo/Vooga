package engine.behavior.collision;

import engine.game_elements.ElementProperty;

/**
 * Deals damage when collided with
 * Could be used for mines, projectiles, etc.
 *
 * @author radithya
 */
public class DamageDealingCollisionVisitable implements CollisionVisitable {

    private double damageToDeal;
    private String audioUrl;
    private double blastRadius;
    
    public DamageDealingCollisionVisitable(
            @ElementProperty(value = "damageToDeal", isTemplateProperty = true) double damageToDeal,
            @ElementProperty(value = "collisionAudioUrl", isTemplateProperty = true) String audioUrl,
            @ElementProperty(value = "blastRadius", isTemplateProperty = true) double blastRadius) {
        this.damageToDeal = damageToDeal;
        this.audioUrl = audioUrl;
        this.blastRadius = blastRadius;
    }

    @Override
    public void accept(CollisionVisitor v) {
        v.visit(this);
    }

    public double getBlastRadius() {
    	return blastRadius;
    }
    public double getDamageToDeal() {
        return damageToDeal;
    }

	@Override
	public String getAudioUrl() {
		return audioUrl;
		
	}

}
