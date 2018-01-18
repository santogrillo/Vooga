package engine.behavior.firing;

import engine.game_elements.ElementProperty;

/**
 * Periodically fires projectiles.
 *
 * @author Ben Schwennesen
 * @author radithya
 * @author tyler
 */
public abstract class AbstractPeriodicFiringStrategy implements FiringStrategy {

    private double attackPeriod;
    private double attackCounter;
    private double range;

    public AbstractPeriodicFiringStrategy(
            @ElementProperty(value = "attackPeriod", isTemplateProperty = true) double attackPeriod,
            @ElementProperty(value = "firingRange", isTemplateProperty = true) double range) {
        this.attackPeriod = attackPeriod;
        this.range = range;
        resetAttackTimer();
    }

    @Override
    public boolean shouldFire(double distanceToTarget) {
        return updateAndCheckTimer() && checkIfWithinRange(distanceToTarget);
    }
    
    @Override
    public boolean isExpended() {
    		return false;
    }

    private boolean updateAndCheckTimer() {
        if (attackCounter-- == 0) {
            resetAttackTimer();
            return true;
        }
        return false;
    }

    private void resetAttackTimer() {
        attackCounter = attackPeriod;
    }

    protected double getAttackPeriod() {
        return attackPeriod;
    }
    
    private boolean checkIfWithinRange(double distanceToTarget) {
    	return (distanceToTarget<=range);
    }
}
