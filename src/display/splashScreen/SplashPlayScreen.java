package display.splashScreen;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import authoring.EditDisplay;
import engine.play_engine.PlayController;
import factory.MediaPlayerFactory;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;
import player.PlayDisplay;

/**
 * Todo - refactor code common with other splash screen
 * @author tyler
 */
public class SplashPlayScreen extends SplashScreen {

	// change this
	public static final String EXPORTED_GAME_PROPERTIES_FILE = "Export.properties";
	private final String EXPORTED_GAME_NAME_KEY = "displayed-game-name";
	// ^

	private final String DEFAULT_GAME_NAME = "Game";
	private final String PLAY = "Play ";

	private static final int PREFSIZE = 80;
	private static final int MAINWIDTH = 1100;
	private static final int MAINHEIGHT = 750;
	private static final int PLAYWIDTH = 1000;
	private static final int PLAYHEIGHT = 700;
	private static final String TITLEFONT = "Verdana";
	private static final String TITLE = "Welcome to VOOGA";
	private static final double STANDARD_PATH_WIDTH = Main.WIDTH / 15;
	private static final double STANDARD_PATH_HEIGHT = Main.HEIGHT / 15;
	
	private HBox titleBox = new HBox();
	private Text VoogaTitle;
	
	private NewGameButton myNewGameButton;
	private EditGameButton myEditGameButton;
	private PlayExistingGameButton myLoadGameButton;
	private MediaPlayerFactory mediaPlayerFactory;
	private MediaPlayer mediaPlayer;


	public SplashPlayScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		String gameName = getGameName();
		getStage().setResizable(false);
		basicSetup();
		myLoadGameButton = new PlayExistingGameButton(this);
		myLoadGameButton.setText(PLAY + gameName);
		rootAdd(myLoadGameButton);
		mediaPlayerFactory = new MediaPlayerFactory("data/audio/101 - opening.mp3");
		mediaPlayer = mediaPlayerFactory.getMediaPlayer();
		if (mediaPlayer != null) {
			mediaPlayer.play();
		}
	}

	private String getGameName() {
		String gameName = DEFAULT_GAME_NAME;
		try {
			Properties gameProperties = new Properties();
			gameProperties.load(getClass().getClassLoader().getResourceAsStream(EXPORTED_GAME_PROPERTIES_FILE));
			gameName = gameProperties.getProperty(EXPORTED_GAME_NAME_KEY);
		} catch (IOException e) {
			// won't happen so ignore (let's hope)
			e.printStackTrace();
		}
		return gameName;
	}





}
