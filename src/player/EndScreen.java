package player;

import display.splashScreen.ScreenDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import main.Main;
import util.PropertiesGetter;

/**
 * 
 * @author mmosca
 *
 */
public abstract class EndScreen extends ScreenDisplay {
	
	private final String BACKGROUND_NAME = "endScreenBackground";
	private final String TITLE_SCREEN = "titleScreen";
	
	public EndScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		setScreenBackground(width, height);
		createScreenTitle(width, height);
	}
	
	private void setScreenBackground(int screenWidth, int screenHeight) {
		
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(BACKGROUND_NAME)));
		ImageView splashBackground = new ImageView(image);
		splashBackground.setFitWidth(screenWidth);
		splashBackground.setFitHeight(screenHeight);
		rootAdd(splashBackground);
	}
	
	private void createScreenTitle(int screenWidth, int screenHeight) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(TITLE_SCREEN)));
		ImageView voogaTitle = new ImageView(image);
		double titleWidth = voogaTitle.getBoundsInLocal().getWidth();
		double titleHeight = voogaTitle.getBoundsInLocal().getHeight();
		double ratio = titleWidth / screenWidth;
		voogaTitle.setFitWidth(screenWidth);
		voogaTitle.setFitHeight(titleHeight / ratio);
		rootAdd(voogaTitle);
	}
	
	protected void addLabel(String text, int screenWidth, int screenHeight) {
		Label screenLabel = new Label(text);
		Label backLayer = new Label(text);
		screenLabel.setLayoutY(200);
		backLayer.setLayoutY(202);
		screenLabel.getStylesheets().add("player/resources/endScreen.css");
		screenLabel.getStyleClass().add("label");
		backLayer.getStylesheets().add("player/resources/endScreen.css");
		backLayer.getStyleClass().add("label2");
		screenLabel.setLayoutX((screenWidth) / 2 - 70 * (text.length() / 2));
		backLayer.setLayoutX((screenWidth) / 2 - 69 * (text.length() / 2));
		rootAdd(backLayer);
		rootAdd(screenLabel);
	}
	
	protected abstract void createButtons();

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listItemClicked(MouseEvent e, ImageView object) {
		// TODO Auto-generated method stub
		
	}
}
