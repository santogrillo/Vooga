package authoring.LevelToolBar;

import javafx.scene.control.Button;

public class EditLevelsButton extends Button {
	
	private static final String LABEL = "Edit Level";
	
	public EditLevelsButton(ButtonInterface level) {
		this.setText(LABEL);
		this.setOnAction(e->level.openLevelDisplay());
	}
}
