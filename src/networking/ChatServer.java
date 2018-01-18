package networking;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javafx.stage.Stage;

/**
 * Manages chat connections and distributes messages.
 *
 * Reference: http://cs.lmu.edu/~ray/notes/javanetexamples/
 *
 * @author Ben Schwennesen
 * @author radithya
 */
public class ChatServer extends AbstractServer {

	public static final int PORT = 9042;
	private Set<PrintWriter> clientPrintWriters = new HashSet<>();

	public ChatServer() {
		super();
	}

	@Override
	public int getPort() {
		return PORT;
	}

	@Override
	protected AbstractServerHandler getServerHandler(Socket acceptSocket) {
		return new ChatServerHandler(acceptSocket, clientPrintWriters);
	}
	
	public static void main(String[] args) {
		ChatServer chatServer = new ChatServer();
		chatServer.startServer();
	}

}