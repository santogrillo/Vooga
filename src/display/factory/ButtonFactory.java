	package display.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class ButtonFactory {
	public static final int DEFAULT_IMAGE_SIZE = 10;
	
	public Button buildDefaultTextButton(String name, EventHandler<ActionEvent> action) {
		Button product = new Button(name);
		product.setOnAction(action);
		return product;
	}
	
	public Button buildDefaultImageButton(ImageView pic, EventHandler<ActionEvent> action) {
		pic.setFitHeight(DEFAULT_IMAGE_SIZE);
		pic.setFitWidth(DEFAULT_IMAGE_SIZE);
		Button product = new Button("", pic);
		product.setOnAction(action);
		return product;
	}
	
	public Button buildCustomTextButton(String name, EventHandler<ActionEvent> action, int fontSize) {
		Button product = new Button(name);
		product.setOnAction(action);
		product.setStyle("-fx-font: " + fontSize + " garamond");
//		product.setPrefWidth(width);
//		product.setPrefHeight(height);
		return product;
	}
	
	public Button buildCustomImageButton(ImageView pic, EventHandler<ActionEvent> action, double imageWidth, double imageHeight) {
		pic.setFitHeight(imageWidth);
		pic.setFitWidth(imageHeight);
		Button product = new Button("", pic);
		product.setOnAction(action);
		return product;
	}
}
