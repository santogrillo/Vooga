package authoring.PropertiesToolBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.splashScreen.ScreenDisplay;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 
 * @author moboyle, bwelton
 *
 */
public class PropertiesTabWithProjectile extends PropertiesTab{
	private PropertiesToolBar myProperties;
	private ProjectileSlot projectileSlot;
	private AuthoringModelController myController;
	private ImageView myImageView;
	
	public PropertiesTabWithProjectile(ScreenDisplay display, PropertiesToolBar properties, ImageView imageView,
									   Map<String, Object> propertyMap, AuthoringModelController author, int upgradeVal) {
		super(display, properties, imageView, propertyMap, author, upgradeVal);
		myController = author;
		myImageView = imageView;
		myProperties = properties;

		Label projectileLabel = new Label("Click to\nChoose a\nprojectile");
		projectileLabel.setLayoutY(90);
		projectileSlot = new ProjectileSlot(this, imageView);
		HBox imageBackground = new HBox();
		imageBackground.setStyle("-fx-background-color: white");
		imageBackground.getChildren().add(imageView);
		if (myController.getAllDefinedTemplateProperties().get(imageView.getId()).get("Projectile Type Name") != null &&
				!myController.getAllDefinedTemplateProperties().get(imageView.getId()).get("Projectile Type Name").toString().isEmpty()) {
			addProjectileImage();
		}
		this.getChildren().add(imageBackground);
		this.getChildren().add(projectileLabel);
		this.getChildren().add(projectileSlot);
	}

	private void addProjectileImage() {
		String projectileName = myController.getAllDefinedTemplateProperties().get(myImageView.getId()).get
				("Projectile Type Name").toString();
		String url = myController.getAllDefinedTemplateProperties().get(projectileName)
				.get("Path of game element image").toString();
		ImageView projectile = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(url)));
		resize(projectileSlot.getPrefHeight(), projectile);
		projectileSlot.getChildren().add(projectile);
	}
	
	protected void newProjectileList(ImageView imageView) {
		ScrollPane projectilesWindow = new ScrollPane();
		ListView<SpriteImage> projectilesView = new ListView<SpriteImage>();
		if (myProperties.getAvailableProjectiles().isEmpty()) {
			Label emptyLabel = new Label("You have no projectiles\nin your inventory");
			this.getChildren().remove(getPropertiesBox());
			emptyLabel.setLayoutX(100);
			this.getChildren().add(emptyLabel);
		} else {
			List<SpriteImage> cloneList = new ArrayList<>();
			for (SpriteImage s : myProperties.getAvailableProjectiles()) {
				cloneList.add(s.clone());
			}
			ObservableList<SpriteImage> items =FXCollections.observableArrayList(cloneList);
	        projectilesView.setItems(items);
	        projectilesView.getSelectionModel();
	        projectilesWindow.setContent(projectilesView);
	        projectilesWindow.setLayoutX(100);
	        projectilesWindow.setPrefHeight(250);
	        projectilesView.setOnMouseClicked(e->projectileSelected(imageView,
	        		projectilesView.getSelectionModel().getSelectedItem().clone()));
	        this.getChildren().remove(getPropertiesBox());
	        this.getChildren().add(projectilesWindow);
		}
	}
	
	
	private void projectileSelected(ImageView imageView, ImageView projectile) {
		projectileSlot.getChildren().removeAll(projectileSlot.getChildren());
		projectileSlot.getChildren().add(projectile);
		Map<String, Object> newProperties = new HashMap<>();
		newProperties.put("Projectile Type Name", projectile.getId());
		myController.updateElementDefinition(imageView.getId(), newProperties, true);
	}
	
	private void resize(double displaySize, ImageView imageView) {
		double spriteWidth = imageView.getBoundsInLocal().getWidth();
		double spriteHeight = imageView.getBoundsInLocal().getHeight();
		double maxDimension = Math.max(spriteWidth, spriteHeight);
		double scaleValue = maxDimension / displaySize;
		imageView.setFitWidth(spriteWidth / scaleValue);
		imageView.setFitHeight(spriteHeight / scaleValue);
	}
}
