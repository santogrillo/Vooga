package authoring.levelEditor;



import java.util.Map;

import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResourceTable {
	private AuthoringModelController myController;
	private TableView<Resource> myTable;
	private int myLv;
	private final int COLUMN_PREF_WIDTH = 120;
	
	public ResourceTable(AuthoringModelController controller, int lv) {
		myController = controller;
		myController.setLevel(lv);
		myLv = lv;
		makeTable();	
		
	}

	public void update() {
		myController.setLevel(myLv);
		myTable.getItems().clear();	
		Map<String, Double> recordedResource = myController.getResourceEndowments();
		makeResources(myTable.getItems(), recordedResource);
	}

	private void makeTable() {
		
		ObservableList<Resource> resources = FXCollections.observableArrayList();
		Map<String, Double> recordedResource = myController.getResourceEndowments();
		makeResources(resources, recordedResource);
		myTable = new TableView<Resource>();
		TableColumn<Resource, ?> myResource = makeColumn("Resource", "resourceType");
		TableColumn<Resource, ?> myAmount = makeColumn("Amount", "amount");
		myTable.setItems(resources);
		myTable.getColumns().addAll(myResource, myAmount);
	}

	private TableColumn<Resource, ?> makeColumn(String title, String instanceVar) {
		TableColumn<Resource, ?> column = new TableColumn<>(title);
		column.setPrefWidth(COLUMN_PREF_WIDTH);
		column.setCellValueFactory(new PropertyValueFactory<>(instanceVar));
		return column;
	}

	private void makeResources(ObservableList<Resource> resources, Map<String, Double> recordedResource) {
		for(String s : recordedResource.keySet()) {
			 Resource r = new Resource(s, recordedResource.get(s));
			 resources.add(r);
		}
		
	}
	
	public TableView<Resource> getTable() {
		return myTable;
	}
	
}
