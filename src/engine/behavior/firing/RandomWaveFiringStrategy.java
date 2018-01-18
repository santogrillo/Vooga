package engine.behavior.firing;

import engine.game_elements.ElementProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Randomly fires various types of sprites into the map based on assigned probabilities.
 *
 * @author Ben Schwennesen
 * @author radithya
 */
public class RandomWaveFiringStrategy extends AbstractWaveFiringStrategy {

    private List<String> templates;
    private List<Double> probabilities;

    public RandomWaveFiringStrategy(
            @ElementProperty(value = "troopProbabilities", isTemplateProperty = true)
                    Map<String, String> troopProbabilities,
            @ElementProperty(value = "attackPeriod", isTemplateProperty = true) double attackPeriod,
            @ElementProperty(value = "totalWaves", isTemplateProperty = true) int totalWaves) {
        super(new ArrayList<>(troopProbabilities.keySet()), attackPeriod, totalWaves);
        templates = new ArrayList<>(troopProbabilities.keySet());
        double cumulativeProbability = 0;
        probabilities = new ArrayList<>();
        for (String templateName : troopProbabilities.keySet()) {
            double directionProbability;
            try {
                directionProbability = Double.parseDouble(troopProbabilities.get(templateName));
            } catch (NumberFormatException nonDouble) {
                directionProbability = 1.0 / troopProbabilities.size();
            }
            cumulativeProbability += directionProbability;
            probabilities.add(cumulativeProbability);
        }
    }

    @Override
    protected String chooseElementToSpawn() {
        // Todo - comment / refactor
        double movementRand = Math.random();
        int insertionPoint = -1 * Collections.binarySearch(probabilities, movementRand) - 1;
        return templates.get(insertionPoint);
    }

	@Override
	public String getAudioUrl() {
		return null;
	}

}
