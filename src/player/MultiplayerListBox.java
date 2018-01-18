package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * 
 * @author mmosca
 *
 */
public class MultiplayerListBox extends ScrollPane {
	private List<String> names;
	private ObservableList<String> namesObservable;
	private ListView<String> namesList;
	
	public MultiplayerListBox() {
		names = new ArrayList<String>();
		namesList = new ListView<String>();
		namesObservable = FXCollections.observableArrayList(names);
		namesList.setItems(namesObservable);
		this.setContent(namesList);
	}
	
	public void attach(VBox lobbiesListBox) {
		lobbiesListBox.getChildren().add(this);
	}
	
	public void setNames(Set<String> content) {
		names.clear();
		for(String name : content)
			names.add(name);
		namesObservable.clear();
		namesObservable.addAll(names);
	}
	
	protected void setNames(List<String> newNames) {
		names.clear();
//		names = newNames;
		for(String name : newNames)
			names.add(name);
		namesObservable.clear();
		namesObservable.addAll(names);
	}
	
	public ListView<String> getListView() {
		return namesList;
	}
}
