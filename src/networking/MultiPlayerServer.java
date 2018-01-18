package networking;

/**
 * Handles requests from multi-player clients over the network
 * 
 * @author radithya
 */
public class MultiPlayerServer extends AbstractGameServer {

	private MultiPlayerController multiPlayerController;

	@Override
	public int getPort() {
		return Constants.MULTIPLAYER_SERVER_PORT;
	}
	
	@Override
	protected AbstractServerController getController() {
		if (multiPlayerController == null) {
			multiPlayerController = new MultiPlayerController();
		}
		return multiPlayerController;
	}

	public static void main(String[] args) {
		MultiPlayerServer multiPlayerServer = new MultiPlayerServer();
		multiPlayerServer.startServer();
	}

}
