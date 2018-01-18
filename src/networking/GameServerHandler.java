package networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameServerHandler extends AbstractServerHandler {

	private AbstractServerController gameController;
	private DataInputStream input;
	private DataOutputStream byteWriter;
	
	public GameServerHandler(Socket socket, AbstractServerController gameController) {
		super(socket);
		this.gameController = gameController;
	}

	public void writeBytes(byte[] bytes) throws IOException {
		byteWriter.writeInt(bytes.length);
		byteWriter.write(bytes, 0, bytes.length);
	}
	
	protected AbstractServerController getController() {
		return gameController;
	}
	
	@Override
	protected void processMessages() throws IOException, ReflectiveOperationException {
		while (true) {
			int len = input.readInt();
			if (len > 0) {
				byte[] readBytes = new byte[len];
				input.readFully(readBytes);
				byte[] response = getController()
						.handleRequestAndSerializeResponse(getSocket().getRemoteSocketAddress().hashCode(), readBytes);
				System.out.println("Writing response of length " + response.length);
				writeBytes(response);
			}
		}
	}

	@Override
	protected void initializeStreams() throws IOException {
		Socket socket = getSocket();
		input = new DataInputStream(socket.getInputStream());
		byteWriter = new DataOutputStream(socket.getOutputStream());
	}

	@Override
	protected void closeClient() {
		super.closeClient();
		getController().disconnectClient(getSocket().getRemoteSocketAddress().hashCode());
	}


}
