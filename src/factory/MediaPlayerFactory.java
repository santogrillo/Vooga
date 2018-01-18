package factory;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

/**
 * @author tyler
 * This class is a factory for 
 */
public class MediaPlayerFactory {
	
	private final String DEFAULT_MEDIA_NAME = "data/audio/497632384.mp4";
	private MediaPlayer mediaPlayer;
	
	/**
	 * Default Constructor
	 */
	public MediaPlayerFactory() {
		mediaPlayer = new MediaPlayer(new Media(composeResourceStringUrl(DEFAULT_MEDIA_NAME)));
		mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
	}
	
	/**
	 * @param mediaName
	 * Constructor that takes a specific media's URL as a string
	 */
	public MediaPlayerFactory(String mediaName) {
		try {
			mediaPlayer = new MediaPlayer(new Media(composeResourceStringUrl(mediaName)));
			mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
			mediaPlayer.setMute(true);
		} catch (MediaException mediaException) {
			// do nothing -- inside JAR
			mediaPlayer = null;
		}
	}
	
	/**
	 * @return
	 * Returns MediaPlayer to be played
	 */
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
	/**
	 * @param newMediaName
	 * Changes the MediaPlayer to what new string input is
	 */
	public void changeMediaPlayer(String newMediaName) {
		if(newMediaName!=null) {
			mediaPlayer = new MediaPlayer(new Media(composeResourceStringUrl(newMediaName)));
		}
	}
	
	private String composeResourceStringUrl(String url) {
		return new File(url).toURI().toString();
	}
}
