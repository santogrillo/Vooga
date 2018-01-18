package util.languageManager;

import java.util.ResourceBundle;

/**
 * 
 * @author venkat
 *
 */
public class AuthorLanguageManager extends LanguageManager {
	private static final String PATH = "/authoring/resources/";
	public AuthorLanguageManager(String language) {
		setResourceBundle(ResourceBundle.getBundle(PATH + language));
	}
	
	public void changeLanguage(String language) {
		setResourceBundle(ResourceBundle.getBundle(PATH+language));
	}
	
}
