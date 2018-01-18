package player;

import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import util.PropertiesGetter;

/**
 * 
 * @author mmosca
 *
 */
public class WinScreen extends EndScreen {
	
	private final String WIN= "win";

	public WinScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		// TODO Auto-generated constructor stub
		addLabel(PropertiesGetter.getProperty(WIN), width, height);
	}

	@Override
	protected void createButtons() {
		// TODO Auto-generated method stub
		
	}

}
