package engine.behavior.firing;

import javafx.geometry.Point2D;

/**
 * Support different ways of firing, one of which is chosen by authoring and
 * then passed to GameElement at initialization
 * 
 * @author tyler
 * @author radithya
 *
 */
public interface FiringStrategy {

	/**
	 * Return the template name of projectile to fire
	 */
	String fire();

	/**
	 * Whether the projectile should fire in this cycle
	 * 
	 * @return
	 */
	boolean shouldFire(double distanceToTarget);

	/**
	 * Whether the firing element has fired its quota and should be removed
	 * 
	 * @return
	 */
	boolean isExpended();
	
	String getAudioUrl();
	
}
