package engine.behavior.firing;

import engine.game_elements.ElementProperty;

/**
 * Captures whatever is common across all implementations of FiringStrategy
 * 
 * @author tyler
 * @author radithya
 *
 */
public abstract class GenericFiringStrategy implements FiringStrategy {

	private String projectileTemplate;
	private String audioUrl;
	private double range;

	public GenericFiringStrategy(
			@ElementProperty(value = "projectileTemplate", isTemplateProperty = true) String projectileTemplate,
			@ElementProperty(value = "firingAudioUrl", isTemplateProperty = true) String audioUrl,
			@ElementProperty(value = "firingRange", isTemplateProperty = true) double range) {
		this.projectileTemplate = projectileTemplate;
		this.audioUrl = audioUrl;
		this.range = range;
	}

	@Override
	public String fire() {
		return projectileTemplate;
	}
	
	@Override
	public boolean isExpended() {
		return false;
	}
	
	@Override
	public String getAudioUrl() {
		return audioUrl;
	}
}
