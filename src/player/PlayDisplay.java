package player;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import display.splashScreen.NotifiableDisplay;
import engine.PlayModelController;

import javafx.stage.Stage;
import networking.protocol.PlayerServer.Notification;

/**
 * Main play display, uses file chooser.
 * @author adi
 */
public class PlayDisplay extends AbstractPlayDisplay implements NotifiableDisplay {

	public PlayDisplay(int width, int height, Stage stage, PlayModelController myController) {
		super(width, height, stage, myController);
		initializeGameState();
	}


	@Override
	public void receiveNotification(byte[] notificationBytes) {
		try {
			Notification playerNotification = Notification.parseFrom(notificationBytes);
			if (playerNotification.hasElementPlaced()) {
				receivePlacedElement(playerNotification.getElementPlaced());
			}
		} catch (InvalidProtocolBufferException e) {
		}
	}

}
