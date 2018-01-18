package engine.authoring_engine;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;

public class AuthoringControllerTesting {

    public static void main(String[] args) {
        AuthoringController authoringController = new AuthoringController();
        System.out.println(authoringController.getElementBaseConfigurationOptions());
        Map<String, String> choices = new HashMap<>();
        choices.put("Movement behavior", "Move in a straight line to a target location");
        choices.put("Collision effects", "Takes damage from collisions");
        choices.put("Collided-with effects", "Deal damage to colliding objects");
        choices.put("Firing Behavior", "Shoot periodically");
        Map<String, Class> auxProperties = authoringController.getAuxiliaryElementConfigurationOptions(choices);
        choices.put("Target y-coordinate", "0.0");
        choices.put("imageWidth", "10.0");
        choices.put("Damage dealt to colliding objects", "1.0");
        choices.put("Health points", "50.0");
        choices.put("Target x-coordinate", "0.0");
        choices.put("imageUrl", "https://pbs.twimg.com/media/CeafUfjUUAA5eKY.png");
        choices.put("Speed of movement", "1");
        choices.put("Numerical \"team\" association", "1");
        choices.put("Attack period", "1");
        choices.put("imageHeight", "10.0");
        choices.put("Projectile Type Name", "fake name");
        Map<String, Object> cc = new HashMap<>(choices);
        authoringController.defineElement("test element", cc);
        JFXPanel jfxPanel = new JFXPanel(); // so that ImageView can be made
        // int id = authoringController.placeElement("test element", new Point2D(0, 0));
    }
}
