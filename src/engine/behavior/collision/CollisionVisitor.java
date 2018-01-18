package engine.behavior.collision;

/**
 * Visitor Design Pattern for handling collisions. Handle effects of collision
 * on colliding element through the visit method
 * 
 * @author radithya
 *
 */
public interface CollisionVisitor {

	// TODO - Consider different kinds of reactions to collisions and implement

	/**
	 * What happens when the CollisionVisitor hits an ImperviousCollisionVisitable
	 * (like an obstacle)
	 * 
	 * @param visitable
	 *            an impervious collision visitable, like an obstacle
	 */
	public void visit(ImperviousCollisionVisitable visitable);

	/**
	 * What happens when the CollisionVisitor hits a DamageDealingCollisionVisitable
	 * (like a mine or projectile)
	 *
	 * @param visitable
	 *            a damage-dealing collision visitable, like a mine or projectile
	 */
	public void visit(DamageDealingCollisionVisitable visitable);

	/**
	 * What happens when the CollisionVisitor hits a NoopCollisionVisitable (like a
	 * tower, soldier, etc)
	 * 
	 * @param visitable
	 *            a collision visitable that can be passed through and has no
	 *            special effect, like a soldier, tower, etc
	 */
	public void visit(NoopCollisionVisitable visitable);

	/**
	 * Whether the collider is 'dead' - mortal colliders, projectiles, etc can be
	 * 'dead'
	 * 
	 * @return
	 */
	public boolean isAlive();

	/**
	 * Whether the unit is blocked by an obstacle Can be used by movement strategy
	 * to recompute path / reverse direction if necessary
	 * 
	 * @return true if unit is blocked, false otherwise
	 */
	public boolean isBlocked();

	/**
	 * Will be used by AbstractMovementStrategy after recomputing path / reversing direction
	 */
	public void unBlock();

	/**
	 * Whether the other CollisionVisitor is an enemy to this one
	 * 
	 * @param other
	 *            CollisionVisitor instance to be checked if enemy or not
	 * @return true if other is an enemy of this CollisionVisitor, false otherwise
	 */
	public boolean isEnemy(CollisionVisitor other);

	/**
	 * Player id of player controlling this CollisionVisitor instance
	 * 
	 * @return int representing id of player controlling this CollisionVisitor
	 *         instance
	 */
	public int getPlayerId();
	
	public String explode();

}
