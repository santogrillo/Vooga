package authoring.PropertiesToolBar;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * 
 * @author moboyle
 *
 */
public class ProjectileSlot extends HBox {

	public ProjectileSlot(PropertiesTabWithProjectile tab, ImageView imageView) {
		this.setPrefWidth(50);
		this.setPrefHeight(50);
		this.setLayoutY(170);
		this.setStyle("-fx-background-color: white");
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, e->tab.newProjectileList(imageView));
	}
}
