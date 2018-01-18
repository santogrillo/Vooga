package authoring;

import authoring.PropertiesToolBar.SpriteImage;
import display.interfaces.ClickableInterface;
import display.interfaces.CreationInterface;
import display.interfaces.CustomizeInterface;
import display.interfaces.TestingInterface;
import javafx.scene.image.ImageView;

public interface AuthorInterface extends CreationInterface, CustomizeInterface, TestingInterface {
	
	public void newTowerSelected(ImageView myImageView);

	public void imageSelected(SpriteImage imageView);

	public void addToMap(String baseProperty, String value);
	
}
