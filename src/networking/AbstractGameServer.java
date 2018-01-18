package networking;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashSet;

public abstract class AbstractGameServer extends AbstractServer {
		
	private Collection<GameServerHandler> handlers;
	
	public AbstractGameServer() {
		handlers = new HashSet<>();
		// Register binding / observable on controller for push notifications
		getController().registerNotificationStreamListener(e -> {
			System.out.println("Processing notification");
			while (e.next()) {
				e.getAddedSubList().stream().forEach(addedMessage -> pushNotification(addedMessage.toByteArray()));				
			}
		});
	}
	
	protected abstract AbstractServerController getController();
	
	protected Collection<GameServerHandler> getHandlers() {
		return handlers;
	}
	
	protected AbstractServerHandler getServerHandler(Socket acceptSocket) {
		GameServerHandler handler = new GameServerHandler(acceptSocket, getController());
		handlers.add(handler);
		return handler;
	}
	
	protected void pushNotification(byte[] notificationBytes) {
		System.out.println("Pushing notification");
		getHandlers().forEach(handler -> {
			try {
				handler.writeBytes(notificationBytes);
			} catch (IOException e) {
				// TODO - handle ?
			}
		});
	}
	
}
