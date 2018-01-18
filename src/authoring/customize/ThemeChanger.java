package authoring.customize;

import authoring.EditDisplay;
import display.interfaces.CustomizeInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import util.PropertiesGetter;
import display.splashScreen.ScreenDisplay;

/**
 * Creates a button to change the background
 * 
 * @author Matt
 */
public class ThemeChanger extends ComboBox<String> {
	
	private final int X_POS = 350;
	private final int Y_POS = 23;
	private final int WIDTH = 200;
	private final int VISIBLE_ROW_COUNT = 3;
	private final String PROMPT_TEXT = "themePrompt";
	public static final String STANDARD = "Standard";
	private final String DARK = "Dark";
	private final String FOREST = "Forest";
	private final String SKY = "Sky";
	private final String GOLD = "Gold";
	private final String MIDNIGHT = "Midnight";
	private final boolean IS_EDITABLE = true;
	
	public ThemeChanger(EditDisplay display) {
		this.setPrefWidth(WIDTH);
		this.setLayoutX(X_POS);
		this.setLayoutY(Y_POS);
		this.setPromptText(PropertiesGetter.getProperty(PROMPT_TEXT));
		String[] themes = {STANDARD, DARK, FOREST, SKY, GOLD, MIDNIGHT};
		ObservableList<String> colorList = FXCollections.observableArrayList(themes);
		ChangeListener<String> propertyHandler = (obs, old, cur) -> display.changeTheme(cur);
		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
		this.setEditable(IS_EDITABLE);
		this.setVisibleRowCount(VISIBLE_ROW_COUNT);
		this.setItems(colorList);
	}
	
	public String getThemePath(String theme) {
		if(theme.equals(FOREST))
			return "authoring/resources/green.css";
		else if(theme.equals(GOLD))
			return "authoring/resources/gold.css";
		else if(theme.equals(SKY))
			return "authoring/resources/blue.css";
		else if(theme.equals(DARK))
			return "authoring/resources/dark.css";
		else if(theme.equals(MIDNIGHT))
			return "authoring/resources/darkpurple.css";
		else
			return "authoring/resources/standard.css";
	}
}
