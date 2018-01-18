package authoring.levelEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.factory.TabFactory;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import factory.AlertFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author venkate, moboyle
 *
 */
public class ResourceDisplay extends VBox{
	private TabPane resourceTabs;
	private List<ResourceTab> resources;
	private Map<String, Double> resourceEndowments;
	private TextField name;
	private TextField value;
	private AuthoringModelController myController;
	private TabFactory tabMaker;
	private final int RESOURCE_DISPLAY_MAX_WIDTH = 250;
	private final boolean IS_CLOSABLE = false;
	private final String RESOURCE_NAME_PROMPT_TEXT = "Name";
	private final String TAB_LEVELS_LABEL = "Level ";
	private final String RESOURCE_VALUE_PROMPT_TEXT = "Value";
	private final String ENTER_BUTTON_LABEL = "add!";
	private final String RESOURCE_ALERT_CONTENT = "You need to input a number!";
	private final String RESOURCE_ALERT_HEADER = "You need to input a number!";
	
	public ResourceDisplay(AuthoringModelController controller){
		myController = controller;
		this.setMaxWidth(RESOURCE_DISPLAY_MAX_WIDTH );
		resourceEndowments = new HashMap<>();
		resourceTabs = new TabPane();
		tabMaker = new TabFactory();
		resources = new ArrayList<ResourceTab>();
		this.getChildren().add(resourceTabs);
		changeResourceValApparatus();
	}

	private void createResourceTabs() {
		for (int i=1; i<=myController.getNumLevelsForGame(); i++) {
//			System.out.println(Integer.toString(myController.getCurrentLevel()));
			Tab newTab = tabMaker.buildTabWithoutContent(TAB_LEVELS_LABEL + Integer.toString(i), null, resourceTabs);
			ResourceTab newLv = new ResourceTab(i, myController);
			newLv.attach(newTab);
			resources.add(newLv);
			final int j = i;
			newTab.setOnSelectionChanged(e->update(j));
			newTab.setClosable(IS_CLOSABLE);
			resourceTabs.getTabs().add(newTab);
		}
		
	}

	private void changeResourceValApparatus() {
		name = new TextField();
		name.setPromptText(RESOURCE_NAME_PROMPT_TEXT);
		value = new TextField();
		value.setPromptText(RESOURCE_VALUE_PROMPT_TEXT);
		Button enter = new Button(ENTER_BUTTON_LABEL);
		enter.setOnAction(e->{
			try {
			if (myController.getResourceEndowments().containsKey(name.getText())) {
				Double d = myController.getResourceEndowments().get(name.getText());
				d = Double.parseDouble(value.getText());
			}
			else {
				resourceEndowments.put(name.getText(), Double.parseDouble(value.getText()));
			}
			try {
				myController.setResourceEndowment(name.getText(), Double.parseDouble(value.getText()));
			} catch(NumberFormatException nfe) {
				new AlertFactory(RESOURCE_ALERT_CONTENT,RESOURCE_ALERT_HEADER,AlertType.ERROR);
			}
			System.out.println(name.getText());
			myController.setResourceEndowment(name.getText(), Double.parseDouble(value.getText()));
			update(myController.getCurrentLevel());
		}catch(Exception nfe) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Input Not Valid");
			a.setContentText("You need to input a number!");
			a.showAndWait().filter(button -> button == ButtonType.OK)
					.ifPresent(event -> a.close());
		}});
		this.getChildren().addAll(name, value, enter);
		
	}

	private void update(int lv) {
		if (resources.size()!=0) {
		resources.get(lv-1).update();
		}
		name.clear();
		value.clear();
		
	}
	
	void updateCurrentState() {
		resources.clear();
		resourceTabs.getTabs().clear();
		createResourceTabs();
		for(int i=0; i<resources.size(); i++) {
			resources.get(i).update();
		}
		myController.setLevel(1);
	}

	public VBox getRoot() {
		return this;
	}
}
