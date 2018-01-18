package authoring.PropertiesToolBar;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PropertiesBoxTest extends VBox{
	
	private TableView table;
	private TableColumn firstCol;
	private TableColumn secondCol;
	private final SimpleStringProperty firstThings;
    private final SimpleStringProperty lastThings;
    private Map<String, String> propertiesMap;
	
    public PropertiesBoxTest() {
    	 table = new TableView();
    	 table.setEditable(true);
    	 
    	 firstThings = new SimpleStringProperty();
    	 lastThings = new SimpleStringProperty();
    	 
    	 propertiesMap.put("Health",  "50");
    	 propertiesMap.put("Strength",  "10");
    	 propertiesMap.put("Other",  "23");

    	 
         TableColumn firstCol = new TableColumn("First Column");
         TableColumn secondCol = new TableColumn("Second Column");
         
         firstCol.setCellFactory(new PropertyValueFactory<SimpleStringProperty, String>("first"));
         
         table.getColumns().addAll(firstCol, secondCol);
         this.setSpacing(50);
         this.getChildren().add(table);
        
    }
}

