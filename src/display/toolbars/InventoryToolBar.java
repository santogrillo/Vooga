package display.toolbars;

import java.util.ArrayList;

import display.factory.TabFactory;
import display.tabs.SimpleTab;
import engine.PlayModelController;
import javafx.scene.control.TabPane;
import display.splashScreen.ScreenDisplay;

/**
 * 
 * @author bwelton
 *
 */
public class InventoryToolBar extends ToolBar{
	private static final int Y_POSITION = 50;
	
	private PlayModelController myController;
	private ScreenDisplay myDisplay;
	private TabFactory tabFactory;
	private SimpleTab towerTab;
	private SimpleTab troopTab;
	private SimpleTab projectileTab;
	
	public InventoryToolBar(ScreenDisplay display, PlayModelController controller) {
		this.setLayoutY(Y_POSITION);
		myController = controller;
		myDisplay = display;
		tabPane = new TabPane();
		tabFactory = new TabFactory();
		this.getChildren().add(tabPane);
        createAndAddTabs();
		this.getStylesheets().add("player/resources/playerPanes.css");
		this.getStyleClass().add("toolbar");
		
	}

	@Override
	protected void createAndAddTabs() {
		towerTab = new SimpleTab(myDisplay, new ArrayList<>());
		troopTab = new SimpleTab(myDisplay, new ArrayList<>());
//		projectileTab = new SimpleTab(myDisplay, new ArrayList<>());
		tabPane.getTabs().add(tabFactory.buildTab("Towers", "TowerImage", towerTab, tabPane));
		tabPane.getTabs().add(tabFactory.buildTab("Troops", "TroopImage", troopTab, tabPane));
//		tabPane.getTabs().add(tabFactory.buildTab("Projectiles", "ProjectileImage", projectileTab, tabPane));
		makeTabsUnclosable(tabPane);
	}
	
	public void initializeInventory() {
		initializeInventory(myController, tabPane);
	}
}
