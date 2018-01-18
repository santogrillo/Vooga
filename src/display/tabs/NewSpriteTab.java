package display.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import authoring.PropertiesToolBar.SpriteImage;
import display.interfaces.CreationInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author mmosca
 *
 */
public abstract class NewSpriteTab extends ScrollPane {
	
	public static final double DISPLAY_SIZE = 60;
	
	private List<SpriteImage> newSpriteImages;
//	private TableView<ImageView> table;
	private ObservableList<SpriteImage> spritesView;
	private ListView<SpriteImage> list;
	private CreationInterface myCreated;
	private ResourceBundle images;
	private SpriteImage spriteImage;
	
	public NewSpriteTab(CreationInterface created) {
		myCreated = created;
		newSpriteImages = new ArrayList<SpriteImage>();
		list = new ListView<SpriteImage>();
		spritesView = FXCollections.observableArrayList(newSpriteImages);
		list.setItems(spritesView);
		this.setContent(list);
		list.setOnMouseClicked(e->myCreated.clicked(
        		list.getSelectionModel().getSelectedItem()));
	}
	
	public Optional<SpriteImage> getImageFromId(String id) {
		return newSpriteImages.stream().filter(newSprite -> newSprite.getId().equals(id)).findFirst();
	}
	
	public void attach(Tab newTroopTab) {
		newTroopTab.setContent(this);
	}
	
	protected List<SpriteImage> getImages() {
		return newSpriteImages;
	}
	
	protected void addImage(SpriteImage spriteImage) {
		spriteImage.resize(DISPLAY_SIZE);
		spritesView.add(spriteImage);
	}
	
	public void tabClicked() {
		list.getSelectionModel().getSelectedItem().addEventHandler
		(MouseEvent.MOUSE_CLICKED, e->myCreated.clicked(
				list.getSelectionModel().getSelectedItem()));
	}
	
	protected abstract void addDefaultImages();
}
