package engine.game_elements;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the upgrading of game elements, including storage of upgrade properties.
 *
 * @author Ben Schwennesen
 */
public class GameElementUpgrader {

    private Map<GameElement, Integer> currentSpriteLevels = new HashMap<>();
    private Map<GameElement, String> spriteTemplateAssociation = new HashMap<>();
    private Map<String, List<Map<String, Object>>> spriteUpgradesByTemplate = new HashMap<>();
    private GameElementFactory gameElementFactory;

    /**
     * Create a sprite upgrader instance to handle sprite upgrades and upgrade definitions.
     *
     * @param gameElementFactory the sprite factory used to create (upgraded) sprites
     */
    public GameElementUpgrader(GameElementFactory gameElementFactory) {
        this.gameElementFactory = gameElementFactory;
    }

    /**
     * Define a new upgrade level for a particular.
     *
     * @param spriteTemplateName the name of the sprite template
     * @param upgradeLevel       the number level this is in the sequence of levels
     * @param upgradeProperties  a map of properties for sprites using this template
     */
    public void defineUpgrade(String spriteTemplateName, int upgradeLevel, Map<String, Object> upgradeProperties) {
        List<Map<String, Object>> templateUpgrades =
                spriteUpgradesByTemplate.getOrDefault(spriteTemplateName, new ArrayList<>());
        System.out.println(upgradeLevel);
        if (upgradeLevel < 0) {
            upgradeLevel = 0;
        } else if (upgradeLevel > templateUpgrades.size()) {
            upgradeLevel = templateUpgrades.size();
        } else if (upgradeLevel >= 0 && upgradeLevel < templateUpgrades.size()) {
            // redefine
            templateUpgrades.remove(upgradeLevel);
        }
        templateUpgrades.add(upgradeLevel, upgradeProperties);
        spriteUpgradesByTemplate.put(spriteTemplateName, templateUpgrades);
    }

    /**
     * Register a newly created gameElement which is potentially eligible for upgrading.
     *
     * @param templateName the base template of a gameElement
     * @param gameElement the gameElement built using the template
     */
    public void registerNewSprite(String templateName, GameElement gameElement) {
        currentSpriteLevels.put(gameElement, 0);
        spriteTemplateAssociation.put(gameElement, templateName);
    }


    /**
     * Upgrade a particular gameElement.
     *
     * @param gameElement the gameElement to upgrade
     * @return the gameElement in its upgraded state
     * @throws IllegalArgumentException if there are no remaining upgrades for the element
     * @throws ReflectiveOperationException if the element cannot be regenerated with its new properties
     */
    public GameElement upgradeSprite(GameElement gameElement)
            throws IllegalArgumentException, ReflectiveOperationException {
        if (!spriteTemplateAssociation.containsKey(gameElement) || !currentSpriteLevels.containsKey(gameElement)) {
            throw new IllegalArgumentException();
        }
        String upgradeTemplateName = spriteTemplateAssociation.get(gameElement);
        int newUpgradeLevel = currentSpriteLevels.get(gameElement);
        if (!canUpgrade(upgradeTemplateName, newUpgradeLevel)) {
            throw new IllegalArgumentException();
        }
        Map<String, ?> upgradeProperties = spriteUpgradesByTemplate.get(upgradeTemplateName).get(newUpgradeLevel);
        Map<String, Object> upgradeArguments = new HashMap<>();
        upgradeArguments.putAll(upgradeProperties);
        // todo - key from prop file/getter
        upgradeArguments.put("startPoint", new Point2D(gameElement.getX(), gameElement.getY()));
        return gameElementFactory.constructElement(upgradeTemplateName, upgradeArguments);
    }

    private boolean canUpgrade(String templateName, int currentUpgradeLevel) {
        return spriteUpgradesByTemplate.containsKey(templateName) &&
                spriteUpgradesByTemplate.get(templateName).size() > currentUpgradeLevel;
    }

    /**
     * Load the sprite upgrade templates from an authored game
     *
     * @param spriteUpgradesByTemplate upgrade templates, loaded from memory, associated with their base templates
     */
    public void loadSpriteUpgrades(Map<String, List<Map<String, Object>>> spriteUpgradesByTemplate) {
        this.spriteUpgradesByTemplate = spriteUpgradesByTemplate;
    }

    /**
     * Retrieve the upgrade templates.
     *
     * @return the sprite upgrade templates for the current game.
     */
    public Map<String, List<Map<String, Object>>> getSpriteUpgradesForEachTemplate() {
        return spriteUpgradesByTemplate;
    }
}
