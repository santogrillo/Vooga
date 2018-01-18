package networking;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		bootServer().startServer();
	}
	
	protected AbstractGameServer bootServer() {
		return new MultiPlayerServer();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
