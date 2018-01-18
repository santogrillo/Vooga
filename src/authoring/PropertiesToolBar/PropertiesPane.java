package authoring.PropertiesToolBar;

import java.util.Map;

import display.factory.TabFactory;
import display.splashScreen.ScreenDisplay;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;

/**
 * 
 * @author moboyle, bwelton
 *
 */
public class PropertiesPane extends TabPane {
	private ScreenDisplay myDisplay;
	private AuthoringModelController myController;
    private ImageView myImageView;
    private PropertiesToolBar myProperties;
    private Tab addTab;
    private TabFactory tabMaker;
    private int upgradeSize = 0;
    private boolean projectile;
	
	public PropertiesPane(ScreenDisplay display, PropertiesToolBar properties, ImageView imageView,
			AuthoringModelController controller, boolean hasProjectile) {
		myImageView = clone(imageView);
		myProperties = properties;
		myController = controller;
		myDisplay = display;
		projectile = hasProjectile;
		tabMaker = new TabFactory();
		
		addTab = new Tab();
		addTab.setClosable(false);
		addTab.setText("Add Upgrade");
		this.getSelectionModel().selectedItemProperty().addListener(e->{
			if(this.getSelectionModel().getSelectedItem() == addTab) {
				Map<String, Object> lastUpdateProperties = (upgradeSize > 1) ?
						myController.getAllDefinedElementUpgrades().get(myImageView.getId()).get(upgradeSize-2) :
							myController.getAllDefinedTemplateProperties().get(myImageView.getId());
				myController.defineElementUpgrade(myImageView.getId(), upgradeSize-2, lastUpdateProperties);
				addUpgrade(lastUpdateProperties);
				this.getSelectionModel().select(upgradeSize);
			}
		});
		
		addUpgrade(myController.getTemplateProperties(imageView.getId()));
		
		if(!myController.getAllDefinedElementUpgrades().isEmpty() &&
				myController.getAllDefinedElementUpgrades().get(imageView.getId()) != null) {
			for(Map<String, Object> upgradeMap : myController.getAllDefinedElementUpgrades().get(imageView.getId())) {
				addUpgrade(upgradeMap);
			}
		}
	}
	
	private void addUpgrade(Map<String, Object> propertyMap) {
		PropertiesTab newTab = (projectile) ? new PropertiesTabWithProjectile(myDisplay, myProperties, clone(myImageView), propertyMap, myController, upgradeSize) 
				: new PropertiesTab(myDisplay, myProperties, clone(myImageView), propertyMap, myController, upgradeSize);
		if(this.getTabs().isEmpty()) {
			this.getTabs().add(tabMaker.buildTab("Base Level", null, newTab, this));
			this.getTabs().get(0).setClosable(false);
			
			this.getTabs().add(addTab);
		}else {
			Tab tab = tabMaker.buildTab("Upgrade " + upgradeSize, null, newTab, this);
			tab.setOnClosed(e->{ upgradeSize--; });
			this.getTabs().add(this.getTabs().size()-1, tab);
		}
		upgradeSize++;
	}
	
	private ImageView clone(ImageView imageView) {
		ImageView cloneImage = new ImageView(imageView.getImage());
		cloneImage.setFitHeight(imageView.getFitHeight());
		cloneImage.setFitWidth(imageView.getFitWidth());
		cloneImage.setId(imageView.getId());
		return cloneImage;
	}

}
