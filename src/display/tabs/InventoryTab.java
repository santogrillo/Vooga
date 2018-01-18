package display.tabs;

import java.util.List;

import authoring.PropertiesToolBar.SpriteImage;
import display.interfaces.PropertiesInterface;
import javafx.scene.image.ImageView;
import display.splashScreen.ScreenDisplay;

/**
 * 
 * @author bwelton
 *
 */
public class InventoryTab extends SimpleTab{
	private PropertiesInterface myProperties;
	
	public InventoryTab(ScreenDisplay display, List<ImageView> defaults, PropertiesInterface properties) {
		super(display, defaults);
		myProperties = properties;
	}
	
	@Override
	protected void addHandler() {
		myListView.setOnMouseClicked(e->{
			if(myListView.getSelectionModel().isEmpty()) return;
			myProperties.clicked(e, myListView.getSelectionModel().getSelectedItem(), this);
		});
	}

}
