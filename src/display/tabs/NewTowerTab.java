package display.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import authoring.EditDisplay;
import authoring.PropertiesToolBar.TowerImage;

/**
 * 
 * @author mmosca
 *
 */
public class NewTowerTab extends NewSpriteTab {
	
	private ResourceBundle towerResources;
	private EditDisplay myDisplay;
	
	public NewTowerTab(EditDisplay display) {
		super(display);
		myDisplay = display;
		towerResources = ResourceBundle.getBundle("authoring/resources/NewTowerImages");
		addDefaultImages();
	}
	
	@Override
	protected void addDefaultImages() {
		List<String> imageList = new ArrayList<String>(Arrays.asList("Black_Square2", "Monkey",
				"Dark_Monkey", "Dark_Monkey2",
				"Green_Turret1", "Green_Turret2", "Green_Turret3", "Green_Turret4",
				"Green_Turret5", "Black_Turret", "Cannon1", "Cannon2", 
				"Castle_Tower1", "Castle_Tower2", "Castle_Tower3", "Castle_Tower4"));
		addImages(imageList);
	}

private void addImages(List<String> stringNames) {
	for (String s : stringNames) {
		addImage(new TowerImage(myDisplay, s));
	}
}
}
