package authoring.PropertiesToolBar;

import authoring.AuthorInterface;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ReturnButton extends Button {

	public ReturnButton(AuthorInterface author) {
		this.setText("Return to main menu");
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->author.returnButtonPressed());
	}
}
