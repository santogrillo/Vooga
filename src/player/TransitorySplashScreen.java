package player;

import engine.PlayModelController;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import util.PropertiesGetter;

/**
 * 
 * @author bwelton
 *
 */
public class TransitorySplashScreen extends BorderPane{
	private final String MESSAGE = "transitoryMessage";
	
	public TransitorySplashScreen(PlayModelController controller) {
		Text loadMessage = new Text();
		loadMessage.setText(PropertiesGetter.getProperty(MESSAGE));
		this.setCenter(loadMessage);
	}
}
