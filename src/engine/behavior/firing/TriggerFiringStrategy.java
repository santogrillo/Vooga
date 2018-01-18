package engine.behavior.firing;

import engine.game_elements.ElementProperty;

public class TriggerFiringStrategy extends GenericFiringStrategy{

	public TriggerFiringStrategy(@ElementProperty(value = "projectileTemplate", isTemplateProperty = true) String projectileTemplate,
			@ElementProperty(value = "firingAudioUrl", isTemplateProperty = true) String audioUrl,
			@ElementProperty(value = "firingRange", isTemplateProperty = true) double range) {
		super(projectileTemplate,audioUrl,range);
	}
	
	@Override
	public boolean shouldFire(double targetLocation) {
		return false;
	}

}
