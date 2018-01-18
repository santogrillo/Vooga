package display.splashScreen;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import main.Main;
import util.PropertiesGetter;

/**
 * Creates a button to move forwards
 * 
 * @author Tyler Yam
 */
public class MuteButton extends Button {
	
	private static final String MUTE_LABEL = "MuteButtonLabel";
	private static final String UNMUTE_LABEL = "UnmuteButtonLabel";
	private static final double WIDTH = Main.WIDTH / 3;
	private static final double XPOS = Main.WIDTH / 2 - WIDTH / 2;
	private static final double YPOS = (8.0 / 9.0) * Main.HEIGHT;
	private MediaPlayer mediaPlayer;
	private boolean muted;


	public MuteButton(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		this.setPrefWidth(WIDTH);
		this.setLayoutX(XPOS);
		this.setLayoutY(YPOS);
		this.setText(PropertiesGetter.getProperty(MUTE_LABEL));
		this.setOnAction(e->mute());
//		this.setStyle(  "-fx-border-color: transparent; -fx-border-width: 0;-fx-background-radius: 0;-fx-background-color: red;");
	}
	
	private void mute() {
		mediaPlayer.setMute(true);
		this.setText(PropertiesGetter.getProperty(UNMUTE_LABEL));
		this.setOnAction(e->unmute());
		muted = true;
	}
	
	private void unmute() {
		mediaPlayer.setMute(false);
		this.setText(PropertiesGetter.getProperty(MUTE_LABEL));
		this.setOnAction(e->mute());
		muted = false;
	}

	public void changeLanguage() {
		if (muted) {
			this.setText(PropertiesGetter.getProperty(UNMUTE_LABEL));
		}
		else {
			this.setText(PropertiesGetter.getProperty(MUTE_LABEL));
		}
		
	}
}