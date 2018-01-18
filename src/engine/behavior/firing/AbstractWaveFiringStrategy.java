package engine.behavior.firing;

import engine.game_elements.ElementProperty;

import java.util.List;

/**
 * @author Ben Schwennesen
 * @author radithya
 *
 */
public abstract class AbstractWaveFiringStrategy extends AbstractPeriodicFiringStrategy {

	private List<String> templatesToFire;
	private int elementsRemaining;

	public AbstractWaveFiringStrategy(
			@ElementProperty(value = "templateToFire", isTemplateProperty = true) List<String> templatesToFire,
			@ElementProperty(value = "spawnPeriod", isTemplateProperty = true) double spawnPeriod,
			@ElementProperty(value = "numberToSpawn", isTemplateProperty = true) int numberToSpawn) {
		super(spawnPeriod,Double.POSITIVE_INFINITY);
		if (templatesToFire.isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.templatesToFire = templatesToFire;
		elementsRemaining = numberToSpawn;
	}

	protected List<String> getTemplatesToFire() {
		return templatesToFire;
	}

	@Override
	public String fire() {
		decrementWavesLeft();
		return chooseElementToSpawn();
	}
	
	@Override
	public boolean shouldFire(double targetLocation) {
		return !this.isExpended() && super.shouldFire(targetLocation);
	}
	
	@Override
	public boolean isExpended() {
		return elementsRemaining <= 0;
	}

	protected abstract String chooseElementToSpawn();
	
	protected int getElementsRemaining() {
		return elementsRemaining;
	}

	protected void decrementWavesLeft() {
		elementsRemaining--;
	}
}
