package authoring.PropertiesToolBar;

import java.util.HashMap;
import java.util.Map;

import display.splashScreen.ScreenDisplay;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * 
 * @author bwelton, moboyle
 *
 */
public class PropertiesTab extends Pane{
	private PropertiesToolBar myProperties;
	private PropertiesBox myPropertiesBox;
	private ImageView myImageView;
	
	public PropertiesTab(ScreenDisplay display, PropertiesToolBar properties, ImageView imageView,
			Map<String, Object> propertyMap, AuthoringModelController controller, int upgradeVal) {
		myProperties = properties;
		myImageView = imageView;
		Map<String, Object> newPropertyMap = new HashMap<>(propertyMap);
		myPropertiesBox = new PropertiesBox(display.getDroppable(), myImageView, newPropertyMap, controller, upgradeVal);
		
		AddToWaveButton myWaveAdder = new AddToWaveButton(myProperties, myImageView);
		CostButton myCost = new CostButton(myProperties, myImageView);
		AddToLevelButton myLevelAdder = new AddToLevelButton(myProperties, myImageView);
		myPropertiesBox.setLayoutX(100);
		
		Button backButton = new Button("Back");
		backButton.setLayoutX(350);
		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->myProperties.removeButtonPressed());
		
		Label info = new Label("Properties here");
		info.setLayoutY(100);
		info.setFont(new Font("Arial", 30));
		
		this.getChildren().add(myImageView);
		this.getChildren().add(backButton);
		this.getChildren().add(myPropertiesBox);
		this.getChildren().add(myWaveAdder);
		this.getChildren().add(myCost);
		this.getChildren().add(myLevelAdder);
	}
	
	protected PropertiesBox getPropertiesBox() {
		return myPropertiesBox;
	}

}
