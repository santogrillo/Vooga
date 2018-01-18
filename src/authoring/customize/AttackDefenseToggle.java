package authoring.customize;

import authoring.EditDisplay;
import display.interfaces.CustomizeInterface;

public class AttackDefenseToggle extends ToggleSwitch {
	private final int ATTACK_DEFENSE_TOGGLE_Y_POS = 550;
	private final int ATTACK_DEFENSE_TOGGLE_WIDTH = 200;
	private final String DEFENSE_LABEL = "Defense";
	private final String ATTACK_LABEL = "Attack";
	
	private EditDisplay myDisplay;
	
	public AttackDefenseToggle(EditDisplay display) {
		this.setLayoutY(ATTACK_DEFENSE_TOGGLE_Y_POS);
		this.setWidth(ATTACK_DEFENSE_TOGGLE_WIDTH);
		myDisplay = display;
		this.setUpSwitch();
	}
	
	private void setUpSwitch() {
		switchedOn.addListener((a,b,c) -> {
			if (c) {
                attack();
            }
			else {
            	defense();
            }
		});
	}

	public void defense() {
		switchLabel.setText(DEFENSE_LABEL);
		setStyle("-fx-background-color: red;");
//		setStyle("-fx-background-color: rgb(66, 123, 230)");
		button.toFront();
		myDisplay.attack();
	}

	public void attack() {
		switchLabel.setText(ATTACK_LABEL);
//		setStyle("-fx-background-color: yellow;");
//		setStyle("-fx-background-color: rgb(240, 200, 100);");
		setStyle("-fx-background-color: rgb(66, 123, 230);");
		switchLabel.toFront();
		myDisplay.defense();
	}
	}
	
	
	

