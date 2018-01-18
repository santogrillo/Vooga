package networking;

import java.io.IOException;
import java.net.Socket;

/**
 * Abstracts common functionality of service handler thread for chat or game
 * information for a single client
 * 
 * @author radithya
 *
 */
public abstract class AbstractServerHandler extends Thread {

	private Socket socket;

	public AbstractServerHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			initializeStreams();
			processMessages();
			return;
		} catch (IOException | ReflectiveOperationException e) {
			// TODO - handle
		} finally {
			closeClient();
		}
	}

	protected abstract void processMessages() throws IOException, ReflectiveOperationException;

	protected abstract void initializeStreams() throws IOException;

	protected void closeClient() {
		try {
			socket.close();
		} catch (IOException e) {
			// do nothing
		}
	}

	protected Socket getSocket() {
		return socket;
	}

}
