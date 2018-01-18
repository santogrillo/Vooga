package display.splashScreen;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import main.Main;
import util.PropertiesGetter;

/**
 * Creates a button to move forwards
 * 
 * @author Matt
 */
public class NewGameButton extends Button {
	
	private static final String LABEL = "NewGameButtonLabel";
	private static final double WIDTH = Main.WIDTH / 3;
	private static final double XPOS = Main.WIDTH / 2 - WIDTH / 2;
	private static final double YPOS = (4.0 / 9.0) * Main.HEIGHT;
	
	public NewGameButton(SplashInterface splash) {
		this.setPrefWidth(WIDTH);
		this.setLayoutX(XPOS);
		this.setLayoutY(YPOS);
		this.setText(PropertiesGetter.getProperty(LABEL));
//		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->splash.newGameButtonPressed());
		this.setOnAction(e->splash.switchScreen());

//		this.setStyle(  "-fx-border-color: transparent; -fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: red;");
	}

	public void changeLanguage() {
		this.setText(PropertiesGetter.getProperty(LABEL));
	}
}
