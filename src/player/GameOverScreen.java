package player;

import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import util.PropertiesGetter;

/**
 * 
 * @author mmosca
 *
 */
public class GameOverScreen extends EndScreen {

	private final String GAME_OVER = "gameOver";
	
	public GameOverScreen(int width, int height, Paint background, Stage currentStage) {
		super(width, height, background, currentStage);
		// TODO Auto-generated constructor stub
		addLabel(PropertiesGetter.getProperty(GAME_OVER), width, height);
	}

	@Override
	protected void createButtons() {
		// TODO Auto-generated method stub
		
	}

}
