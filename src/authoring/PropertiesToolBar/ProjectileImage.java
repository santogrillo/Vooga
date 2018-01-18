package authoring.PropertiesToolBar;

import java.util.ResourceBundle;

import display.splashScreen.ScreenDisplay;


public class ProjectileImage extends SpriteImage {
	
	private ScreenDisplay myDisplay;
	private ResourceBundle projectileResources;
	private String myKey;
	
	public ProjectileImage(ScreenDisplay display, String stringKey) {
		super(display);
		myDisplay = display;
		myKey = stringKey;
		projectileResources = ResourceBundle.getBundle("authoring/resources/NewProjectileImages");
		if(projectileResources.containsKey(stringKey)) {
			this.addImage(projectileResources.getString(stringKey));
		}else {
			this.addImage(stringKey);
		}

	}
	
	@Override
	public ProjectileImage clone() {
		ProjectileImage cloneImage = new ProjectileImage(myDisplay, myKey);
		cloneImage.setName(this.getName());
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		cloneImage.setMyProperties(this.getMyProperties());
		return cloneImage;
	}
}
