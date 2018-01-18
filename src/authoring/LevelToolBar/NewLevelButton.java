package authoring.LevelToolBar;

import javafx.scene.control.Button;

public class NewLevelButton extends Button {
	
	private static final String LABEL = "New level";
	
	public NewLevelButton(ButtonInterface level) {
		this.setText(LABEL);
		this.setOnAction(e->level.addLevel());
	}

}
