package authoring.PropertiesToolBar;

import display.interfaces.PropertiesInterface;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author bwelton
 *
 */
public class CostButton extends Button {

	public CostButton(PropertiesToolBar toolbar, ImageView currentElement) {
		this.setText("Add Unit Cost");
		this.setLayoutX(350);
		this.setLayoutY(80);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->toolbar.setObjectCost(currentElement.getId()));
		
	}
}
