package authoring;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * 
 * @author mmosca
 *
 */
public class BasePropertySelector extends ComboBox<String> {
	private static final int X_POS = 350;
	private static final int Y_POS = 23;
	private static final int WIDTH = 200;
	
	private String propertyName;
	private List<String> propertyOptions;
	
	public BasePropertySelector(BasePropertyMediator mediator, String name, List<String> options) {
		propertyName = name;
		propertyOptions = options;
		this.setPrefWidth(WIDTH);
		this.setLayoutX(X_POS);
		this.setLayoutY(Y_POS);
		this.setPromptText(propertyName);
		ObservableList<String> propertyList = FXCollections.observableArrayList(propertyOptions);
		ChangeListener<String> propertyHandler = (obs, old, cur) -> mediator.addBaseProperty(propertyName, cur);
		this.getSelectionModel().selectedItemProperty().addListener(propertyHandler);
		this.setEditable(true);
		this.setVisibleRowCount(3);
		this.setItems(propertyList);
	}
}
