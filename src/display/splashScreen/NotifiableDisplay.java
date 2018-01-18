package display.splashScreen;

import javafx.scene.Scene;
import networking.protocol.PlayerServer.LevelInitialized;

public interface NotifiableDisplay {
	
	public void  receiveNotification(byte[] messageBytes);

	public void startDisplay(LevelInitialized levelData);
	
	public Scene getScene();
	
	public String getGameState();
	
	
}
