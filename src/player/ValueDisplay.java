package player;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 
 * @author mmosca
 *
 */
public class ValueDisplay extends HBox {
	private final double IMAGE_WIDTH= 40;
	private final double IMAGE_HEIGHT= 32;
	
	private Label myValue;
	private Label myLabel;
	private ImageView myValueImage;
	private double quantity;
	
	public ValueDisplay() {
		quantity = 0;
		myValue = new Label(Double.toString(quantity));
		setStandardDisplayValue();
		setStandardBoxStyle();
		this.setPadding(new Insets(0, 20, 0, 20));
//		this.getStyleClass().add("coin-display");
	}
	
	protected void addItemsWithImage() {
		this.getChildren().add(myValueImage);
		this.getChildren().add(myValue);
	}
	
	protected void addItemsWithLabel() {
		this.getChildren().add(myLabel);
		this.getChildren().add(myValue);
	}
	
	protected void setStandardDisplayImage(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		myValueImage = new ImageView(image);
		myValueImage.setFitWidth(40);
		myValueImage.setFitHeight(32);
	}
	
	protected void setStandardDisplayLabel(String labelText) {
		myLabel = new Label(labelText + ": ");
//		myLabel.setLayoutX(0);
//		myLabel.setFont(new Font(30));
//		myLabel.setTextFill(Color.WHITE);
		myLabel.getStylesheets().add("player/resources/valueDisplay.css");
		myLabel.getStyleClass().add("label");
	}
	
	protected void setStandardDisplayValue() {
		myValue.getStylesheets().add("player/resources/valueDisplay.css");
		myValue.getStyleClass().add("label");
	}
	
	protected void setStandardImageViewSize() {
		myValueImage.setFitWidth(IMAGE_WIDTH);
		myValueImage.setFitHeight(IMAGE_HEIGHT);
	}
	
	private void setStandardBoxStyle() {
		this.getStylesheets().add("player/resources/valueDisplay.css");
		this.getStyleClass().add("coin-display");
	}
	
	protected void setBoxPosition(double x, double y) {
		this.setLayoutX(x);
		this.setLayoutY(y);
	}
	
	public void increment() {
		quantity++;
		myValue.setText(Double.toString(quantity));
	}
	
	public void decrease() {
		if (quantity >= 5) {
			quantity -= 5;
		}
		myValue.setText(Double.toString(quantity));
	}

	public boolean decreaseByAmount(double amount) {
		if(quantity >= amount)
			quantity -= amount;
		return quantity >= amount;
	}
	
	public void increaseByAmount(double amount) {
		quantity += amount;
	}
	
	public void setValue(double amount) {
		myValue.setText(Double.toString(amount));
		if(amount >= 0)
			quantity = amount;
	}
	
	protected double getQuantity() {
		return quantity;
	}
	
	public void reset() {
		quantity = 0;
	}
}
