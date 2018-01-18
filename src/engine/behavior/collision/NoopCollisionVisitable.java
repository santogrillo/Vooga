package engine.behavior.collision;

/**
 * Does not have any special effect on the collider when he/she collides with it
 * Could be used by towers, soldiers, etc, basically most sprites
 * @author radithya
 *
 */
public class NoopCollisionVisitable implements CollisionVisitable {

	@Override
	public void accept(CollisionVisitor v) {
		v.visit(this);
	}

	@Override
	public String getAudioUrl() {
		return null;
	}
	
	public double getBlastRadius() {
		return 0;
	}

}
