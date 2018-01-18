package player;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import util.PropertiesGetter;

/**
 * 
 * @author mmosca
 *
 */
public class ChangeSpeedToggles {
	private static final int BUTTON_WIDTH = 45;
	private static final int BUTTON_HEIGHT = 40;
	private static final String PLAY_OFF = "playOff";
	private static final String PLAY_ON = "playOn";
	private static final String PAUSE_OFF = "pauseOff";
	private static final String PAUSE_ON = "pauseOn";
	private static final String FAST_OFF = "fastOff";
	private static final String FAST_ON = "fastOn";
//	private static final String PLAY_OFF = "gray_play1.png";
//	private static final String PLAY_ON = "gray_play2.png";
//	private static final String PAUSE_OFF = "gray_pause1.png";
//	private static final String PAUSE_ON = "gray_pause2.png";
//	private static final String FAST_OFF = "gray_fast1.png";
//	private static final String FAST_ON = "gray_fast2.png";
	
	private Image playOnImage;
	private Image playOffImage;
	private Image pauseOnImage;
	private Image pauseOffImage;
	private Image fastOnImage;
	private Image fastOffImage;
	private ImageView playButton;
	private ImageView pauseButton;
	private ImageView fastButton;
	
	public ChangeSpeedToggles() {
		setPlayImages();
		setPauseImages();
		setFastImages();
		playButton = new ImageView(playOnImage);
		pauseButton = new ImageView(pauseOffImage);
		fastButton = new ImageView(fastOffImage);
		setButtonSizes(BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	private void setPlayImages() {
		playOffImage = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(PLAY_OFF)));
		playOnImage = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(PLAY_ON)));
	}
	
	private void setPauseImages() {
		pauseOffImage = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(PAUSE_OFF)));
		pauseOnImage = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(PAUSE_ON)));
	}
	
	private void setFastImages() {
		fastOffImage = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(FAST_OFF)));
		fastOnImage = new Image(getClass().getClassLoader().getResourceAsStream(PropertiesGetter.getProperty(FAST_ON)));
	}
	
	public void setPlayMouseEvent(EventHandler<? super MouseEvent> playGame) {
		playButton.setOnMouseClicked(playGame);
	}
	
	public void setPauseMouseEvent(EventHandler<? super MouseEvent> pauseGame) {
		pauseButton.setOnMouseClicked(pauseGame);
	}
	
	public void setFastMouseEvent(EventHandler<? super MouseEvent> speedUpGame) {
		fastButton.setOnMouseClicked(speedUpGame);
	}
	
	public void orchestratePlay() {
		playButton.setImage(playOnImage);
		pauseButton.setImage(pauseOffImage);
		fastButton.setImage(fastOffImage);
	}
	
	public void orchestratePause() {
		playButton.setImage(playOffImage);
		pauseButton.setImage(pauseOnImage);
		fastButton.setImage(fastOffImage);
	}
	
	public void orchestrateFast() {
		playButton.setImage(playOffImage);
		pauseButton.setImage(pauseOffImage);
		fastButton.setImage(fastOnImage);
	}
	
	private void setButtonSizes(int width, int height) {
		playButton.setFitWidth(width);
		playButton.setFitHeight(height);
		pauseButton.setFitWidth(width);
		pauseButton.setFitHeight(height);
		fastButton.setFitWidth(width);
		fastButton.setFitHeight(height);
	}
	
	public ImageView getPlay() {
		return playButton;
	}
	
	public ImageView getPause() {
		return pauseButton;
	}
	
	public ImageView getFast() {
		return fastButton;
	}
}
