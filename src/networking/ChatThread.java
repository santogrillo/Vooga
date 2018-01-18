package networking;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Used to poll for other participants' messages.
 *
 * Based on http://www.geeksforgeeks.org/a-group-chat-application-in-java/.
 *
 * @author Ben Schwennesen
 */
public class ChatThread extends Thread {

    private final String SOCKET_ERROR = "There was an error accessing the server connection. Ensure setup is correct.";
    private final String SIGN_OFF = "Goodbye!";

    private Socket socket;
    private ObservableList<Node> chatItems;
    private BufferedReader in;

    public ChatThread(Socket socket, ObservableList<Node> chatItems) {
        this.socket = socket;
        this.chatItems = chatItems;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException socketInputException) {
            Text errorText = new Text(SOCKET_ERROR);
            errorText.setFill(Color.RED);
            chatItems.add(chatItems.size(), errorText);
        }

    }

    @Override
    public void run() {
        while(!socket.isClosed()) {
            String message;
            try {
                if ((message = in.readLine()) != null) {
                    Platform.runLater(() -> chatItems.add(chatItems.size(), new Text(message)));
                }
            } catch (IOException socketClosed) {
                Platform.runLater(() -> chatItems.add(chatItems.size(), new Text(SIGN_OFF)));
            }
        }
    }
}