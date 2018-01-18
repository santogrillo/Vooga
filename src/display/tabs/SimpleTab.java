package display.tabs;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import display.splashScreen.ScreenDisplay;

/**
 * 
 * @author bwelton
 *
 */
public class SimpleTab extends ScrollPane{
	private ScreenDisplay display;
	private List<ImageView> myList;
	protected ListView<ImageView> myListView;
	private ObservableList<ImageView> items;
	
	public SimpleTab(ScreenDisplay display, List<ImageView> defaults) {
		this.display = display;
		addDefaultImages(defaults);
	}

	private void addDefaultImages(List<ImageView> defaults) {
		myList = defaults;
		items = FXCollections.observableArrayList(myList);
		myListView = new ListView<>();
		addHandler();
		myListView.setItems(items);
		this.setContent(myListView);
	}
	
	protected void addHandler() {
		myListView.setOnMouseClicked(e->display.listItemClicked(e, 
	      		myListView.getSelectionModel().getSelectedItem()));
	}
	
	public void addItem(ImageView object) {
		items.add(object);
	}
	
	public boolean removeItem(ImageView object) {
		return items.remove(object);
	}
	
	public List<ImageView> getImages(){
		return new ArrayList<>(myList);
	}
}
