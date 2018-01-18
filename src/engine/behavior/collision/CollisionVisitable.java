package engine.behavior.collision;

/**
 * Visitor Design Pattern for handling collisions. Classes implementing
 * CollisionVisitable represent different kinds of effects on CollisionVisitors
 * when they collide with something
 * 
 * @author radithya
 *
 */
public interface CollisionVisitable {

	/**
	 * Facilitate double-dispatch
	 * 
	 * @param v
	 *            the CollisionVisitor who collides with this CollisionVisitable
	 */
	public void accept(CollisionVisitor v);
	
	public String getAudioUrl();
	
	public double getBlastRadius();
}
