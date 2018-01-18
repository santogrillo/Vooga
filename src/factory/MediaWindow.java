package factory;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author tyler
 * This class creates a window to view MediaPlayers videos 
 */
public class MediaWindow {

	private double width;
	private double height;
	private double centeredX;
	
	private MediaPlayer mediaPlayer;
	private MediaView mediaViewer;
	private Stage mediaStage;
	private Group root = new Group();
	private Button skipButton;
	MediaPlayerFactory mediaPlayerFactory;
	
	/**
	 * @param mediaName
	 * Sets up window as well as the MediaPlayer and MediaView.
	 * The skip button is also created here
	 */
	public MediaWindow(String mediaName) {
		mediaStage = new Stage();
		setStageToScreen();
		
		mediaPlayerFactory = new MediaPlayerFactory(mediaName);
		mediaPlayer = mediaPlayerFactory.getMediaPlayer();
		mediaViewer = new MediaView(mediaPlayer);
		mediaViewer.setFitWidth(width);
		mediaViewer.setFitHeight(height);
		centeredX= width/7;
		mediaViewer.setX(centeredX);
		skipButton = new Button("skip");
		skipButton.setOnAction(e->skip());
	}

	/**
	 * Sets up stage to fit the screen of the computer
	 */
	private void setStageToScreen() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		width = primaryScreenBounds.getWidth();
		height = primaryScreenBounds.getHeight();
		mediaStage.setX(primaryScreenBounds.getMinX());
		mediaStage.setY(primaryScreenBounds.getMinY());
		mediaStage.setWidth(width);
		mediaStage.setHeight(height);
	}
	
	/**
	 * Plays the MediaPlayer as well as displays the stage
	 */
	public void play() {
		root.getChildren().add(mediaViewer);
		root.getChildren().add(skipButton);
		Scene scene = new Scene(root, width, height, Color.BLACK);
		mediaStage.setTitle("Media");
		mediaStage.setScene(scene);
		mediaStage.show();
		mediaPlayer.play();
		mediaStage.setOnCloseRequest(e->mediaPlayer.stop());
	}
	
	/**
	 * Closes out of the MediaWindow
	 */
	public void skip() {
		mediaPlayer.stop();
		mediaStage.close();
	}
}
