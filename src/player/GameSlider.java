package player;

import java.io.IOException;

import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class GameSlider extends Slider {
	
	private final int MIN = 0;
	private final int MAX = 1;
	private final int LAYOUT_Y = 600;
	private final int PREF_WIDTH = 300;
	private final int MAJOR_TICK_UNIT = 200;
	private final int MINOR_TICK_UNIT = 20;
	private final int BLOCK_INCREMENT = 10;
	private final boolean SHOW_TICK_LABELS = true;
	private final boolean SHOW_TICK_MARKS = true;
	
	public GameSlider(PlayerInterface player) {
		this.setMin(MIN);
		this.setMax(MAX);
		this.setLayoutY(LAYOUT_Y);
		this.setPrefWidth(PREF_WIDTH);
		this.setShowTickLabels(SHOW_TICK_LABELS);
		this.setShowTickMarks(SHOW_TICK_MARKS);
		this.setMajorTickUnit(MAJOR_TICK_UNIT);
		this.setMinorTickCount(MINOR_TICK_UNIT);
		this.setBlockIncrement(BLOCK_INCREMENT);
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->System.out.println(this.getValue()));
	}
	
	public void incrementWidth() {
		this.setMax(this.getMax() + 1);
	}
}