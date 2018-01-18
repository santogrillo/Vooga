package networking;

public class CollabServerMain extends ServerMain {

	@Override
	public AbstractGameServer bootServer() {
		return new CollaborativeAuthoringServer();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
