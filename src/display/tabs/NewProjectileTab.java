package display.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import authoring.EditDisplay;
import authoring.PropertiesToolBar.ProjectileImage;

/**
 * 
 * @author mmosca
 *
 */
public class NewProjectileTab extends NewSpriteTab {
	
	private ResourceBundle projectileResources;
	private EditDisplay myDisplay;
	
	public NewProjectileTab(EditDisplay display) {
		super(display);
		myDisplay = display;
		projectileResources = ResourceBundle.getBundle("authoring/resources/NewProjectileImages");
		addDefaultImages();
	}
	
	@Override
	protected void addDefaultImages() {
		List<String> imageList = new ArrayList<String>(Arrays.asList("Gray_Circle",
				"Black_Square", "Black_Square2", "Orange_Splash", "Fireball"));
		addImages(imageList);
	}
	
	private void addImages(List<String> stringNames) {
		for (String s : stringNames) {
			addImage(new ProjectileImage(myDisplay, s));
		}
	}
}
