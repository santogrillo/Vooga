package authoring.PropertiesToolBar;

import java.util.ResourceBundle;

import display.splashScreen.ScreenDisplay;


public class TowerImage extends SpriteImage {
	
	private ScreenDisplay myDisplay;
	private ResourceBundle towerResources;
	private String myKey;
	private SpriteImage myProjectile;
	
	public TowerImage(ScreenDisplay display, String stringKey) {
		super(display);
		myDisplay = display;
		myKey = stringKey;
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		if(towerResources.containsKey(stringKey)) {
			this.addImage(towerResources.getString(stringKey));
		}else {
			this.addImage(stringKey);
		}
	}
	
	public void addProjectileImage(SpriteImage newProjectile) {
		myProjectile = newProjectile;
	}
	
	public SpriteImage getProjectileImage() {
		return myProjectile;
	}
	
	public boolean hasProjectile() {
		return myProjectile != null;
	}
	
	@Override
	public TowerImage clone() {
		TowerImage cloneImage = new TowerImage(myDisplay, myKey);
		cloneImage.setName(this.getName());
		cloneImage.setFitHeight(this.getFitHeight());
		cloneImage.setFitWidth(this.getFitWidth());
		cloneImage.setMyProperties(this.getMyProperties());
		return cloneImage;
	}
}
