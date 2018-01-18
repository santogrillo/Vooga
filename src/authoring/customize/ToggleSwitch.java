package authoring.customize;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class ToggleSwitch extends HBox {
	
	protected final Label	switchLabel = new Label();
	protected final Button button = new Button();
	private final int TOGGLE_SWITCH_WIDTH = 80;
	
	protected SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
	public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
	
	private void init() {
		
		switchLabel.setText("Defense");
		
		getChildren().addAll(switchLabel, button);	
		button.setOnAction((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		switchLabel.setOnMouseClicked((e) -> {
			switchedOn.set(!switchedOn.get());
		});
		setStyle();
		bindProperties();
	}
	
	private void setStyle() {
		//Default Width
		setWidth(TOGGLE_SWITCH_WIDTH);
		switchLabel.setAlignment(Pos.CENTER);
		setStyle("-fx-background-color: red; -fx-text-fill:black; -fx-background-radius: 4;");
		setAlignment(Pos.CENTER_LEFT);
	}
	
	private void bindProperties() {
		switchLabel.prefWidthProperty().bind(widthProperty().divide(2));
		switchLabel.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}
	
	public ToggleSwitch() {
		init();
	}
}