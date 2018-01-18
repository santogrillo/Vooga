package display.splashScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import authoring.EditDisplay;
import engine.authoring_engine.AuthoringController;
import engine.play_engine.PlayController;
import factory.MediaPlayerFactory;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ChoiceDialog;
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
import networking.CollaborativeAuthoringClient;
import networking.MultiPlayerClient;
import player.MultiplayerLobby;
import player.PlayDisplay;
import util.PropertiesGetter;

/**
 * 
 * @author mmosca
 *
 */
public class SplashScreen extends ScreenDisplay implements SplashInterface {

	private static final int PREFSIZE = 80;
	private static final int MAINWIDTH = 1100;
	private static final int MAINHEIGHT = 750;
	private static final String TITLEFONT = "Verdana";
	private static final String TITLE = "TowerDefenseTitleLabel";
	public static final String SPLASHTITLE = "SplashTitle";
	private static final double STANDARD_PATH_WIDTH = Main.WIDTH / 15;
	private static final double STANDARD_PATH_HEIGHT = Main.HEIGHT / 15;
	private static final String SINGLE_PLAYER = "Single Player";
	private static final String MULTIPLAYER = "Multiplayer";
	private static final String PLAY_CHOICE_DIALOG_STRING = "Players options";
	private static final String PLAY_TITLE_STRING = "Players Setting";
	private static final String PLAY_HEADER_STRING = "Single player or multiplayer?";
	private static final String AUTHOR_CHOICE_DIALOG_STRING = "Authoring options";
	private static final String AUTHOR_TITLE_STRING = "Authoring Setting";
	private static final String AUTHOR_HEADER_STRING = "Single author or collaborative editing?";

	private HBox titleBox = new HBox();
	private Text VoogaTitle;
	private NewGameButton myNewGameButton;
	private EditGameButton myEditGameButton;
	private PlayExistingGameButton myLoadGameButton;
	private MuteButton myMuteButton;
	private MediaPlayerFactory myMediaPlayerFactory;
	private MediaPlayer myMediaPlayer;
	private ChangeLanguageDropDown myLanguageChanger;
	private String backgroundSong = "data/audio/101 - opening.mp3";
	private Label title;

	public SplashScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);

		getStage().setResizable(false);
		basicSetup();
		myNewGameButton = new NewGameButton(this);
		rootAdd(myNewGameButton);
		myEditGameButton = new EditGameButton(this);
		rootAdd(myEditGameButton);
		myLoadGameButton = new PlayExistingGameButton(this);
		rootAdd(myLoadGameButton);
		myLanguageChanger = new ChangeLanguageDropDown(this);
		rootAdd(myLanguageChanger);
		myMediaPlayerFactory = new MediaPlayerFactory(backgroundSong);
		myMediaPlayer = myMediaPlayerFactory.getMediaPlayer();
		if (myMediaPlayer != null) {
			myMediaPlayer.play();
			myMuteButton = new MuteButton(myMediaPlayer);
		}
		rootAdd(myMuteButton);
	}

	protected void basicSetup() {
		// createTitle();
		setSplashBackground();
		createPathTitle();
		createSubtitle();
		addPath();
	}

	private void createTitle() {
//		VoogaTitle = new Text(10, 20, TITLE);
		VoogaTitle.setFont(Font.font(TITLEFONT, FontPosture.ITALIC, 30));
		VoogaTitle.setFill(Color.DARKBLUE);
		// VoogaTitle.setFill(Color.GOLD);
		// VoogaTitle.setFill(Color.SILVER);
		titleBox = new HBox();
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(VoogaTitle);
		titleBox.setPrefSize(PREFSIZE, PREFSIZE);
		rootAdd(titleBox);
	}

	private void setSplashBackground() {
		String backgroundName = "grass_large.png";
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(backgroundName));
		ImageView splashBackground = new ImageView(image);
		splashBackground.setFitWidth(Main.WIDTH);
		splashBackground.setFitHeight(Main.HEIGHT);
		rootAdd(splashBackground);
	}

	private void createPathTitle() {
		String titleName = "VOOGA_Words.png";
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(titleName));
		ImageView voogaTitle = new ImageView(image);
		double width = voogaTitle.getBoundsInLocal().getWidth();
		double height = voogaTitle.getBoundsInLocal().getHeight();
		double ratio = width / Main.WIDTH;
		voogaTitle.setFitWidth(Main.WIDTH);
		voogaTitle.setFitHeight(height / ratio);
		rootAdd(voogaTitle);
	}

	private void createSubtitle() {
		title = new Label(PropertiesGetter.getProperty(TITLE));
		title.setFont(new Font("American Typewriter", Main.WIDTH / 40));
		title.setTextFill(Color.BLACK);
		title.setLayoutX(Main.WIDTH / 10);
		title.setLayoutY(Main.HEIGHT / 3);
		rootAdd(title);
	}

	private void addPath() {
		for (int i = 0; i < 5; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2);
		}
		for (int i = 0; i < 3; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * 4, Main.HEIGHT / 2 + (i + 1) * STANDARD_PATH_HEIGHT);
		}
		for (int i = 4; i < 11; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2 + 4 * STANDARD_PATH_HEIGHT);
		}
		// Next two for asymmetric style
		for (int i = 2; i < 3; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * 10, Main.HEIGHT / 2 + (i + 1) * STANDARD_PATH_HEIGHT);
		}
		for (int i = 10; i < 15; i++) {
			createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2 + 2 * STANDARD_PATH_HEIGHT);
		}
		// Next two for symmetric style
		// for(int i = 0; i < 3; i++) {
		// createStandardPath(STANDARD_PATH_WIDTH * 10, Main.HEIGHT / 2 + (i +
		// 1) *
		// STANDARD_PATH_HEIGHT);
		// }
		// for(int i = 10; i < 15; i++) {
		// createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2);
		// }
		// First two plus next one for third path style
		// for(int i = 4; i < 15; i++) {
		// createStandardPath(STANDARD_PATH_WIDTH * i, Main.HEIGHT / 2 + 4 *
		// STANDARD_PATH_HEIGHT);
		// }
	}

	private ImageView createStandardPath(double xPos, double yPos) {
		String pathName = "brick_path.png";
		// String pathName = "stone_path2.png";
		Image pathImage = new Image(getClass().getClassLoader().getResourceAsStream(pathName));
		ImageView path = new ImageView(pathImage);
		path.setFitWidth(STANDARD_PATH_WIDTH);
		path.setFitHeight(STANDARD_PATH_HEIGHT);
		path.setX(xPos);
		path.setY(yPos);
		rootAdd(path);
		return path;
	}

	// TODO - REFACTOR

	@Override
	public void editButtonPressed() {
		boolean isMultiplayer = initializeEditingSetting();
		if (isMultiplayer) {
			initializeMultiPlayerEditDisplay(true);
		} else {
			initializeSinglePlayerEditDisplay(true);
		}
		myMediaPlayer.stop();
	}

	@Override
	public void newGameButtonPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	public void switchScreen() { // called by New
		boolean isMultiplayer = initializeEditingSetting();
		if (isMultiplayer) {
			initializeMultiPlayerEditDisplay(false);
		} else {
			initializeSinglePlayerEditDisplay(false);
		}
		myMediaPlayer.stop();
	}

	@Override
	public void playExisting() {
		boolean isMultiplayer = initializePlayersSetting();
		PlayDisplay myScene;
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - PLAYWIDTH / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - PLAYHEIGHT / 2);
		myMediaPlayer.stop();
		if (!isMultiplayer) {
			myScene = new PlayDisplay(PLAYWIDTH, PLAYHEIGHT, getStage(), new PlayController());
			getStage().setScene(myScene.getScene());
			myScene.startDisplay();
		} else {
			MultiPlayerClient multiPlayerClient = new MultiPlayerClient();
			multiPlayerClient.launchNotificationListener();
			myScene = new PlayDisplay(PLAYWIDTH, PLAYHEIGHT, getStage(), multiPlayerClient);
			MultiplayerLobby multi = new MultiplayerLobby(PLAYWIDTH, PLAYHEIGHT, Color.WHITE, getStage(), myScene,
					multiPlayerClient);
			getStage().setScene(multi.getScene());
			multi.promptForUsername();
		}
	}

	private boolean initializePlayersSetting() {
		return initializeSetting(PLAY_CHOICE_DIALOG_STRING, PLAY_TITLE_STRING, PLAY_HEADER_STRING);
	}

	private boolean initializeEditingSetting() {
		return initializeSetting(AUTHOR_CHOICE_DIALOG_STRING, AUTHOR_TITLE_STRING, AUTHOR_HEADER_STRING);
	}

	private boolean initializeSetting(String choiceDialogString, String titleString, String headerString) {
		List<String> numPlayers = new ArrayList<String>();
		String settingChoice = new String();
		numPlayers.add(SINGLE_PLAYER);
		numPlayers.add(MULTIPLAYER);
		ChoiceDialog<String> playerSetting = new ChoiceDialog<>(choiceDialogString, numPlayers);
		playerSetting.setTitle(titleString);
		playerSetting.setHeaderText(headerString);
		playerSetting.setContentText(null);
		Optional<String> result = playerSetting.showAndWait();
		if (result.isPresent()) {
			settingChoice = result.get();
		}
		return settingChoice.equals(MULTIPLAYER);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void listItemClicked(MouseEvent e, ImageView object) {
		// TODO Auto-generated method stub

	}
	
	private void initializeSinglePlayerEditDisplay(boolean loaded) {
		EditDisplay myScene = new EditDisplay(MAINWIDTH, MAINHEIGHT, getStage(), loaded, new AuthoringController());
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - MAINWIDTH / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - MAINHEIGHT / 2);
		myScene.startDisplay();
		getStage().setScene(myScene.getScene());
	}

	private void initializeMultiPlayerEditDisplay(boolean loaded) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - MAINWIDTH / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - MAINHEIGHT / 2);
		CollaborativeAuthoringClient collabClient = new CollaborativeAuthoringClient();
		collabClient.launchNotificationListener();
		EditDisplay myScene = new EditDisplay(PLAYWIDTH, PLAYHEIGHT, getStage(), loaded, collabClient);
		System.out.println("Initialized EditDisplay");
		MultiplayerLobby multi = new MultiplayerLobby(PLAYWIDTH, PLAYHEIGHT, Color.WHITE, getStage(), myScene,
				collabClient);
		getStage().setScene(multi.getScene());
		multi.promptForUsername();
	}

	public void updateLanguage() {
		getStage().setTitle(PropertiesGetter.getProperty(SPLASHTITLE));
		myNewGameButton.changeLanguage();
		myEditGameButton.changeLanguage();
		myMuteButton.changeLanguage();
		myLoadGameButton.changeLanguage();
		title.setText(PropertiesGetter.getProperty(TITLE));
		
	}

}
