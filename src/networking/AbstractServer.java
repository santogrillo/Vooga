package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Abstracts common server functionality for Multi-Player game server and chat
 * server
 * 
 * @author radithya
 *
 */
public abstract class AbstractServer {

	private ServerSocket listener;

	public AbstractServer() {
		try {
			listener = new ServerSocket(getPort());
		} catch (IOException e) {
			// todo - handle
		}
	}

	public abstract int getPort();

	public void startServer() {
		System.out.println("Server is running...");
		try {
			while (true) {
				AbstractServerHandler serverHandler = getServerHandler(listener.accept());
				serverHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			// do nothing
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				// do nothing
				e.printStackTrace();
			}
		}
	}

	protected abstract AbstractServerHandler getServerHandler(Socket acceptSocket);

}
