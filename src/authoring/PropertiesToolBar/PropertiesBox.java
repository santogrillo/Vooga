package authoring.PropertiesToolBar;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import engine.AuthoringModelController;
import util.path.Path;
import util.path.PathList;
import util.path.PathParser;
import display.interfaces.Droppable;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author moboyle, bwelton
 *
 */
public class PropertiesBox extends VBox {
	private Map<String, Object> propertiesMap;
	private String[] propertyArr;
	private TableView<Properties> table;
	private ObservableList<Properties> data;
	private TableColumn<Properties, String> propertiesColumn;
	private TableColumn<Properties, String> valuesColumn;
	private ImageView currSprite;
	private AddToWaveButton myWaveAdder;
	private Droppable myDroppable;
	private int upgradeLevel;
	
	public PropertiesBox(Droppable droppable, ImageView mySprite, Map<String, Object> propertyMap,
			AuthoringModelController author, int upgradeVal) {
		currSprite = mySprite;
		myDroppable = droppable;
		propertiesMap = propertyMap;
		upgradeLevel = upgradeVal;
		table = new TableView<>();
		table.setEditable(true);
		propertiesColumn = new TableColumn<>("Properties");
		valuesColumn = new TableColumn<>("Values");
		data = FXCollections.observableArrayList();
		
		for (String propertyName : propertiesMap.keySet()) {
			data.add(new Properties(propertyName, propertiesMap.get(propertyName)));
		}
		
		FXCollections.sort(data, new Sortbyname());
		
		propertiesColumn.setCellValueFactory(
				new PropertyValueFactory<>("myProperty"));
		valuesColumn.setCellValueFactory(
				new PropertyValueFactory<>("myValue"));
		table.setItems(data);
		table.getColumns().addAll(propertiesColumn, valuesColumn);
		this.getChildren().add(table);
		table.setPrefHeight(250);
		this.setPrefHeight(250);
		this.setLayoutX(50);
		
		valuesColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		valuesColumn.setOnEditStart(
				 new EventHandler<CellEditEvent<Properties, String>>() {

					@Override
					public void handle(CellEditEvent<Properties, String> t) {
						if(t.getRowValue().getMyProperty().equals("Path to follow")) {
							String filePath = new String();
							Random rand = new Random();
							PathParser parser = new PathParser();
							Path path = launchPathSelection();
							List<PathList> possiblePaths = parser.parse(path);
							try {
								filePath = possiblePaths.get(rand.nextInt(possiblePaths.size())).writeToSerializationFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							((Properties) t.getTableView().getItems().get(
					                t.getTablePosition().getRow())
					                ).setMyObject(filePath);

				            propertiesMap.put(t.getRowValue().getMyProperty(), t.getRowValue().getMyObject());
				            if(upgradeVal != 0) {
			            			author.defineElementUpgrade(mySprite.getId(), upgradeLevel-1, propertiesMap);
				            }else {
			            			author.updateElementDefinition(mySprite.getId(), propertiesMap, true);
				            }
						}
					}  
				 }       
			);
		valuesColumn.setOnEditCommit(
			    new EventHandler<CellEditEvent<Properties, String>>() {
			        @Override
			        
			        public void handle(CellEditEvent<Properties, String> t) {
			        		if(t.getRowValue().getMyProperty().equals("Path to follow")) return;
			            t.getTableView().getItems().get(t.getTablePosition().getRow()).setMyValue(t.getNewValue());
			            propertyMap.put(t.getRowValue().getMyProperty(), t.getRowValue().getMyObject());
			            if(upgradeVal != 0) {
			            		author.defineElementUpgrade(mySprite.getId(), upgradeLevel-1, propertiesMap);
			   
			            }else {
			            		author.updateElementDefinition(mySprite.getId(), propertiesMap, true);
			            }
			            
			        }
			    }
			);
	}
	
	public ImageView getCurrSprite() {
		return currSprite;
		
	}
	
	private Path launchPathSelection() {
		Dialog<String> pathChooser = new Dialog<>();
		pathChooser.setTitle("Path Selection");
		pathChooser.setContentText("Choose a path for the object to follow");
		
		BorderPane pane = new BorderPane();
		pane.setPrefSize(150, 100);
		ComboBox<Rectangle> colorChooser = new ComboBox<>();
		colorChooser.setPrefSize(150, 30);
		Map<Path, Color> paths = myDroppable.getPaths();
		for(Path path:paths.keySet()) {
			Rectangle r = new Rectangle(colorChooser.getPrefWidth()-30, colorChooser.getPrefHeight());
			r.setFill(paths.get(path));
			colorChooser.getItems().add(r);
		}
		pane.setCenter(colorChooser);
		pathChooser.getDialogPane().setContent(pane);
		pathChooser.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		Optional<String> result = pathChooser.showAndWait();
		
		while(!result.isPresent()) {
			result = pathChooser.showAndWait();
		}

		for(Path path:paths.keySet()) {
			if(paths.get(path) != null
					&& paths.get(path).equals(colorChooser.getSelectionModel().getSelectedItem().getFill())) {
				return path;
			}
		}
		return null;
	}
}

/* @author sgrillo
 * 
 */
class Sortbyname implements Comparator<Properties>
{
    // Used for sorting in ascending order of
    // roll name
    public int compare(Properties a, Properties b)
    {
        return a.getMyProperty().compareTo(b.getMyProperty());
    }
}