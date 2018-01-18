package util.languageManager;

import java.util.ResourceBundle;
/*
 * @author venk
 */
public class LanguageManager {

	private ResourceBundle currentLanguage;
	
	
	public LanguageManager() {
	}
	
	protected ResourceBundle getResourceBundle() {
		return currentLanguage;
	}
	
	protected void setResourceBundle(ResourceBundle rb) {
		currentLanguage = rb;
	}
	
}
