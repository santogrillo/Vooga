package authoring.LevelToolBar;

import javafx.scene.control.Button;

public class NewWaveButton extends Button {
	
	private static final String LABEL = "New wave";
	
	public NewWaveButton(ButtonInterface level) {
		this.setText(LABEL);
		this.setOnAction(e->level.makeNewWave());
	}

}
