package authoring.PropertiesToolBar;

import display.interfaces.CreationInterface;
import display.interfaces.PropertiesInterface;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AddToWaveButton extends Button {
	
	public AddToWaveButton(PropertiesInterface property, ImageView imageView) {
		this.setText("Add to wave");
		this.setLayoutX(350);
		this.setLayoutY(50);
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->property.addToWave(imageView));
		
	}
	

}
