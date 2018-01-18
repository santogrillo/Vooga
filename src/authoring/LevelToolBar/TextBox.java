package authoring.LevelToolBar;

import javafx.scene.control.TextField;
@Deprecated
public abstract class TextBox {
	private TextField myText;
	private String myString;
	
	public TextBox(String s) {
		myText = new TextField();
		myString = s;
	}
	
	public void recordInfo() {
		/* this would be an inherited method that is overriden in each individual textbox, 
		 * allowing for the update of that particular backend element.
		 * Clearly, since this is an abstract class, this would never really be called.
		 */
		return;
	}
	
	public TextField getTextField() {
		return myText;
	}
	
	public String getString() {
		return myString;
	}
}
