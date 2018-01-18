package factory;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertFactory {
	public AlertFactory(String message, Alert.AlertType type){
		Alert alert = new Alert(type);
		setContentAndShow(alert,message);
    }
	
	public AlertFactory(String message, String header, Alert.AlertType type){
		Alert alert = new Alert(type);
        alert.setHeaderText(header);
        setContentAndShow(alert,message);
    }
	
	public AlertFactory(String message, String header, String title, Alert.AlertType type){
		Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setTitle(title);
        setContentAndShow(alert,message);
    }
	
	private void setContentAndShow(Alert alert, String message) {
		alert.setContentText(message);
        alert.showAndWait().filter(press -> press == ButtonType.OK)
                .ifPresent(event -> alert.close());
	}
}
