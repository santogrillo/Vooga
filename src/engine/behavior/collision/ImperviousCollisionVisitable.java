package engine.behavior.collision;

/**
 * Impenetrable, blocks objects that collide with it, forcing them to change
 * direction
 * Could be used in obstacles
 * 
 * @author radithya
 *
 */
public class ImperviousCollisionVisitable implements CollisionVisitable {

	// TODO - Customize the 'deflecting' behavior of these impervious visitables
	// through either initialization params or sub-classing?

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
