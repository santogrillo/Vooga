package display.splashScreen;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import util.PropertiesGetter;

/*
 * @author venkat
 * Class that provides ability to change languages in the game.
 */
public class ChangeLanguageDropDown extends VBox {
	private static final String BUTTON_LABEL = "languageButtonLabel";
	private static final String LABEL = "languageChangerLabel";
	private static final String ENGLISH = "English";
	private static final String FRENCH = "French";
	
	private ComboBox<String> myBox;
	private Button update;
	private SplashScreen myScreen;
	
	public ChangeLanguageDropDown(SplashScreen splashScreen){
		myScreen = splashScreen;
		myBox = new ComboBox<String>();
		myBox.setPromptText(PropertiesGetter.getProperty(LABEL));
		myBox.getItems().addAll(ENGLISH, FRENCH);
		update = new Button();
		update.setText(PropertiesGetter.getProperty(BUTTON_LABEL));
		update.setOnAction(e->{if (myBox.getValue()!=null) {
			PropertiesGetter.setLanguageFiles(myBox.getValue());
		}
		myScreen.updateLanguage();
		update.setText(PropertiesGetter.getProperty(BUTTON_LABEL));
		myBox.setValue(null);
		myBox.setPromptText(PropertiesGetter.getProperty(LABEL));
		});
		this.getChildren().addAll(myBox, update);
	}
	
}
