package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

/**
 * Handles a chat connection with a client, broadcasting this client's messages
 * to all clients
 * 
 * @author radithya
 *
 */
public class ChatServerHandler extends AbstractServerHandler {

	// Set of writers passed down from ChatServer overseeing multiple
	// ChatServerHandlers
	private Set<PrintWriter> clientPrintWriters;
	private BufferedReader input;

	public ChatServerHandler(Socket socket, Set<PrintWriter> clientPrintWriters) {
		super(socket);
		this.clientPrintWriters = clientPrintWriters;
	}

	@Override
	protected void processMessages() throws IOException {
		while (true) {
			String message = input.readLine();
			if (message == null) {
				return;
			}
			synchronized (clientPrintWriters) {
				for (PrintWriter writer : clientPrintWriters) {
					writer.println(message);
				}
			}
		}
	}

	@Override
	protected void initializeStreams() throws IOException {
		Socket socket = getSocket();
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		synchronized (clientPrintWriters) {
			clientPrintWriters.add(new PrintWriter(socket.getOutputStream(), true));
		}
	}

}
