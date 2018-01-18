package authoring.customize;

import display.interfaces.CustomizeInterface;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import util.PropertiesGetter;

public class ColorChanger extends ColorPicker {
	
	private final int Y_POS = 500;
	private final int WIDTH = 200;
	private final String PROMPT_TEXT = "backgroundChooser";
	private final int HEX_MULTIPLIER = 255;
	
	
	public ColorChanger(CustomizeInterface customize) {
		this.setPrefWidth(WIDTH);
		this.setLayoutY(Y_POS);
		this.setPromptText(PropertiesGetter.getProperty(PROMPT_TEXT));
		this.setOnAction(e-> customize.changeColor(toRGBCode(this.getValue())));
	}
	
	//https://stackoverflow.com/questions/17925318/how-to-get-hex-web-string-from-javafx-colorpicker-color
	private String toRGBCode(Color color){
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * HEX_MULTIPLIER ),
            (int)( color.getGreen() * HEX_MULTIPLIER ),
            (int)( color.getBlue() * HEX_MULTIPLIER ) );
    }
}
