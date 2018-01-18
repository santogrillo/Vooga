package factory;

import java.io.File;

import javafx.scene.media.AudioClip;

/**
 * @author tyler
 * This class is a factory to made sound effects for the game
 * such as projectiles firing and exploding
 */
public class AudioClipFactory {
	
	private final String DEFAULT_AUDIO_NAME = "data/audio/Pew_Pew-DKnight556-1379997159.mp3";
	private final double DEFAULT_VOLUME = 20;
	private AudioClip audioClip;
	//TO DO make default_volume to universal volume
	
	/**
	 * Default constructor
	 */
	public AudioClipFactory() {
		audioClip = new AudioClip(composeResourceStringUrl(DEFAULT_AUDIO_NAME));
		audioClip.setVolume(DEFAULT_VOLUME);
	}
	
	/**
	 * @param name
	 * Constructor that takes in a specific audio clip's URL as a string
	 */
	public AudioClipFactory(String name) {
		audioClip = new AudioClip(composeResourceStringUrl(name));
		audioClip.setVolume(DEFAULT_VOLUME);
	}
	
	/**
	 * @return
	 * returns AudioClip to be played
	 */
	public AudioClip getAudioClip() {
		return audioClip;
	}
	
	/**
	 * @param url
	 * @return
	 * formats String name into acceptable form to create
	 * the AudioClip
	 */
	private String composeResourceStringUrl(String url) {
		return new File(url).toURI().toString();
	}
}
