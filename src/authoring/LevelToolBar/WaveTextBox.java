package authoring.LevelToolBar;import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Deprecated
public class WaveTextBox extends TextBox {

	public WaveTextBox(String s) {
		super(s);
	}

	@Override
	public void recordInfo() {
		//this is where the information would be sent to the backend regarding the entry made.
	}
}
