package player;

import java.util.TimerTask;

import javafx.scene.layout.HBox;

public class NotificationTimerTask extends TimerTask {
	private HBox notificationBox;
	private HBox notification;
	
	public NotificationTimerTask(HBox box, HBox note) {
		notificationBox = box;
		notification = note;
	}
	
	@Override
	public void run() {
		notificationBox.getChildren().remove(notification);
	}

}
