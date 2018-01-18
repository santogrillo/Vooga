package util;

import engine.game_elements.ElementOptionsGetter;

import java.util.HashMap;
import java.util.Map;

public class SpriteOptionsGetterTesting {

    public static void main(String[] args) {
        ElementOptionsGetter spriteTranslator = new ElementOptionsGetter();
        Map<String, String> choices = new HashMap<>();
        choices.put("engine.behavior.movement.MovementStrategy", "Move in a straight line to a target location");
        choices.put("engine.behavior.collision.CollisionVisitor", "Invulnerable to collision damage");
        choices.put("engine.behavior.firing.FiringStrategy", "Do not fire projectiles");
        choices.put("engine.behavior.collision.CollisionVisitable", "Deal damage to colliding objects");
        System.out.println(spriteTranslator.getAuxiliaryParametersFromSubclassChoices(choices));
    }
}
