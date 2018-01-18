package authoring.PropertiesToolBar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import authoring.EditDisplay;
import display.tabs.AddSpriteImageTab;
import display.tabs.AddTab;
import display.tabs.InventoryTab;
import display.tabs.NewProjectileTab;
import display.tabs.NewSpriteTab;
import display.tabs.NewTowerTab;
import display.tabs.NewTroopTab;
import display.tabs.SimpleTab;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import display.factory.TabFactory;
import display.interfaces.CreationInterface;
import display.interfaces.PropertiesInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import display.splashScreen.ScreenDisplay;
import display.toolbars.ToolBar;
 
/**
 * 
 * @author bwelton, moboyle, mmosca
 *
 */
public class PropertiesToolBar extends ToolBar implements PropertiesInterface {
	
	private TabFactory tabMaker;
	private TabPane topTabPane;
	private TabPane bottomTabPane;
	private AddTab addTab;
	private NewSpriteTab newTower;
	private NewSpriteTab newTroop;
	private NewSpriteTab newProjectile;
	private SimpleTab inventoryTower;
	private SimpleTab inventoryTroop;
	private SimpleTab inventoryProjectile;
	private ScreenDisplay myDisplay;
	private AddNewButton myNewButton;
	private PropertiesBox myPropertiesBox;
	private PropertiesPane propertiesPane;
	private Label projectileLabel;
	private HBox projectileSlot;
	private Button deleteButton;
	//private ReturnButton retB;
	private CreationInterface created;
	private AuthoringModelController myController;
	private Map<String, Object> basePropertyMap;
	private EditDisplay display;
    private AddToWaveButton myWaveAdder;
    private CostButton myCost;
    private AddToLevelButton myLevelAdder;
    private VBox myVBox;
    private Stage waveStage;

	private List<SpriteImage> availableProjectiles;

	private final int X_LAYOUT = 680;
	private final int Y_LAYOUT = 30;

	
	public PropertiesToolBar(EditDisplay display, AuthoringModelController controller) {
		this.display = display;
		myDisplay = display;
		//retB = new ReturnButton(display);
		myController = controller;
		availableProjectiles = new ArrayList<>();
		newTower = new NewTowerTab(display);   
	    newTroop = new NewTroopTab(display); 
	    newProjectile = new NewProjectileTab(display); 
	    tabMaker = new TabFactory();
	    topTabPane = new TabPane();
	    bottomTabPane = new TabPane();
	    topTabPane.setPrefHeight(250);
	    bottomTabPane.setPrefHeight(250);
	    
        this.setLayoutX(X_LAYOUT);
		this.setLayoutY(Y_LAYOUT);
		this.setSpacing(20);
	    createAndAddTabs();
	    
	    
	    myNewButton = new AddNewButton(created);
        this.getChildren().add(topTabPane);
        this.getChildren().add(bottomTabPane);
        //this.getChildren().add(retB);
        
        
        newTower.attach(topTabPane.getTabs().get(0));
        newTroop.attach(topTabPane.getTabs().get(1));
        newProjectile.attach(topTabPane.getTabs().get(2));
        
        basePropertyMap = new HashMap<>();
        initializeInventory(myController, bottomTabPane);
    }
	
	public Optional<SpriteImage> getImageOfAppropriateTypeFromId(String id) {
		Optional<SpriteImage> towerImage = newTower.getImageFromId(id);
		if (towerImage.isPresent()) {
			return towerImage;
		}
		Optional<SpriteImage> troopImage = newTroop.getImageFromId(id);
		if (troopImage.isPresent()) {
			return troopImage;
		}
		return newProjectile.getImageFromId(id);
	}
	
	@Override
	protected void createAndAddTabs() {
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Tower", "TowerImage", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Troop", "TroopImage", topTabPane));
		topTabPane.getTabs().add(tabMaker.buildTabWithoutContent("New Projectile", "ProjectileImage", topTabPane));
		addTab = new AddSpriteImageTab(null , topTabPane);
		topTabPane.getTabs().add(tabMaker.buildTab("Add Image", null, addTab, topTabPane));
		
		inventoryTower = new InventoryTab(myDisplay, new ArrayList<>(), this);
		inventoryTroop = new InventoryTab(myDisplay, new ArrayList<>(), this);
		inventoryProjectile = new InventoryTab(myDisplay, new ArrayList<>(), this);
		bottomTabPane.getTabs().add(tabMaker.buildTab("Towers", "TowerImage", inventoryTower, bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTab("Troops", "TroopImage", inventoryTroop, bottomTabPane));
		bottomTabPane.getTabs().add(tabMaker.buildTab("Projectiles", "ProjectileImage", inventoryProjectile, bottomTabPane));
		makeTabsUnclosable(topTabPane);
		makeTabsUnclosable(bottomTabPane);
	}
	
	@Override
	public void imageSelected(SpriteImage myImageView) {
		if (myImageView instanceof TowerImage) inventoryTower.addItem(myImageView.clone());
		if (myImageView instanceof TroopImage) inventoryTroop.addItem(myImageView.clone());
		if (myImageView instanceof ProjectileImage) {
			inventoryProjectile.addItem(myImageView.clone());
			availableProjectiles.add(myImageView);
		}
	}

	@Override
	public void clicked(MouseEvent e, ImageView imageView, SimpleTab tab) {	
		if(e.getButton() == MouseButton.SECONDARY) {
			myController.deleteElementDefinition(imageView.getId());
			tab.removeItem(imageView);
		}else {
			Map<String, Map<String, Object>> templatePropertiesMap = myController.getAllDefinedTemplateProperties();
			System.out.println("Size of template properties map: " + templatePropertiesMap.keySet().size());
			String tabType =
					templatePropertiesMap.get(imageView.getId()).get("tabName").toString();
			if (tabType.equals("Towers")) {
				newPane(imageView, true);
			}else {
				newPane(imageView, false);
			}
		}
	}

	private void newPane(ImageView imageView, boolean hasProjectile) {
		this.getChildren().clear();
		propertiesPane = new PropertiesPane(display, this, imageView, myController, hasProjectile);
		this.getChildren().add(propertiesPane);
		this.getChildren().add(bottomTabPane);
	}
	
	protected void removeButtonPressed() {
		this.getChildren().removeAll(this.getChildren());
		this.getChildren().add(topTabPane);
		this.getChildren().add(bottomTabPane);
		//this.getChildren().add(retB);
	}
	
	public void addToMap(String property, String value) {
		basePropertyMap.put(property, value);
	}
	
	@Override
	public void addToWave(ImageView imageView) {
		int maxLevel = myController.getNumLevelsForGame();
		waveStage = new Stage();
		waveStage.setTitle("CheckBox Experiment 1");
        myVBox = new VBox();
        createTextFields(imageView);
        Scene scene = new Scene(myVBox, 200, 120);
        waveStage.setScene(scene);
        waveStage.show();
	}
	
	private void createTextFields(ImageView imageView) {
        TextField waveAndLevelField = new TextField();
        waveAndLevelField.setPromptText("Which level and waves? Seperate by commas");
        TextField amountField = new TextField();
        amountField.setPromptText("How many of this Sprite?");
        TextField locationField = new TextField();
        locationField.setPromptText("Where should this wave spawn? (Split with comma)");
        myVBox.getChildren().add(waveAndLevelField);
        myVBox.getChildren().add(amountField);
        myVBox.getChildren().add(locationField);
        Button submitButton = new Button("Submit");
//        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
//        		e->submitToWaves(waveAndLevelField.getText().split("\\s+"), 
//        				Integer.valueOf(amountField.getText())));
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
        		e->submitToWaves(waveAndLevelField.getText(), locationField.getText(),
        				Integer.valueOf(amountField.getText()), imageView));
        myVBox.getChildren().add(submitButton);
	}
	
	private void submitToWaves(String levelsAndWaves, String location, int amount, ImageView imageView) {
//        for (int i = 0; i < levelsAndWaves.length; i++) {
//        	String[] currLevelAndWave = levelsAndWaves[i].split("\\.");
//    		display.submit(Integer.valueOf(currLevelAndWave[0]), 
//    				Integer.valueOf(currLevelAndWave[1]), amount, clone(myPropertiesBox.getCurrSprite()));
//        }
		display.submit(levelsAndWaves, location, amount, imageView);
		waveStage.hide();
	}
	
	//TODO refactor and remove from right toolbar
	protected void setObjectCost(String elementName) {
		Map<String, Double> unitCosts = myController.getElementCosts().get(elementName);
		Map<String, Double> currentEndowments = myController.getResourceEndowments();
		Dialog<String> costDialog = new Dialog<>();
		costDialog.setTitle("Unit Cost");
		costDialog.setHeaderText("Assign resource costs to your unit");
		
		BorderPane pane = new BorderPane();
		HBox resources = new HBox();
		
		TextField amount = new TextField();
		ComboBox<String> resourceNames = new ComboBox<>();
		for(String resource : currentEndowments.keySet()) {
			resourceNames.getItems().add(resource);
		}
		resourceNames.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(unitCosts.get(resourceNames.getSelectionModel().getSelectedItem()) != null) {
					amount.setText(Double.toString(unitCosts.get(resourceNames.getSelectionModel().getSelectedItem())));
				}else {
					amount.setText("0.0");
				}
				
			}
		});
		
		Button update = new Button();
		update.setText("Update");
		update.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
			try{
				unitCosts.put(resourceNames.getSelectionModel().getSelectedItem(), Double.parseDouble(amount.getText()));
			}catch(NumberFormatException e) {
				unitCosts.put(resourceNames.getSelectionModel().getSelectedItem(), 0.0);
			}
		});
		
		resources.getChildren().add(resourceNames);
		resources.getChildren().add(amount);
		resources.getChildren().add(update);
		pane.setPrefSize(300, 75);
		pane.setCenter(resources);
		costDialog.getDialogPane().setContent(pane);
		costDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		Optional<String> result = costDialog.showAndWait();
		
		if(result.isPresent()) {
			myController.setUnitCost(elementName, unitCosts);
		}
	}
	
	private ImageView clone(ImageView imageView) {
		ImageView cloneImage = new ImageView(imageView.getImage());
		cloneImage.setFitHeight(imageView.getFitHeight());
		cloneImage.setFitWidth(imageView.getFitWidth());
		cloneImage.setId(imageView.getId());
		return cloneImage;
	}

	@Override
	public void addToLevel(ImageView imageView) {
		int maxLevel = myController.getNumLevelsForGame();
		Stage levelStage = new Stage();
		levelStage.setTitle("CheckBox Experiment 1");
        VBox myVBox = new VBox();

        for (int i = 1; i <= maxLevel; i++) {
        	CheckBox myCheckBox = new CheckBox(Integer.toString(i));
        	myVBox.getChildren().add(myCheckBox);
        }
        Button submitButton = new Button("Submit");	
        submitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->submitToLevel(myVBox, levelStage, imageView));
        myVBox.getChildren().add(submitButton);
        Scene scene = new Scene(myVBox, 200, 50 + 20*maxLevel);
        levelStage.setScene(scene);
        levelStage.show();
	}

	private void submitToLevel(VBox myVBox, Stage levelStage, ImageView imageView) {
		for (Node n : myVBox.getChildren()) {
			if (n instanceof CheckBox) {
				CheckBox c = (CheckBox) n;
				if (c.isSelected()) {				
						display.addToBottomToolBar(Integer.valueOf(c.getText()), clone(imageView), 2);

				}
			}
		}
		levelStage.close();
	}
	
	protected List<SpriteImage> getAvailableProjectiles(){
		return availableProjectiles;
	}
} 