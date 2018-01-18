package authoring.levelEditor;





import java.util.Map;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class ResourceTab extends ScrollPane{
	private AuthoringModelController myController;
	private ResourceTable resourceTable;
	
	public ResourceTab(int i, AuthoringModelController controller) {
		myController = controller;
		resourceTable = new ResourceTable(myController, i);
		this.setContent(resourceTable.getTable());	
	}
	
	public void attach(Tab level) {
		level.setContent(this);
	}
	
	public void update() {
		resourceTable.update();
	}
	
}
