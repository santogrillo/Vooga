package networking;

public class CollaborativeAuthoringServer extends AbstractGameServer {

	private CollaborativeAuthoringController controller;
	
	@Override
	public int getPort() {
		return Constants.COLLABORATIVE_AUTHORING_SERVER_PORT;
	}

	@Override
	protected AbstractServerController getController() {
		if (controller == null) {
			controller = new CollaborativeAuthoringController();
		}
		return controller;
	}
	
	public static void main(String[] args) {
		CollaborativeAuthoringServer collabServer = new CollaborativeAuthoringServer();
		collabServer.startServer();
	}


}
