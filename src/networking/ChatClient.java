package networking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Chat client that connects to the chat server and sends the client's messages.
 *
 * @author Ben Schwennesen
 */
public class ChatClient {

    private final String SOCKET_ERROR = "Error creating socket. Double check that you entered the right IP for " +
            "the chat server.";
    private final String MESSAGE_DELIMITER = ": ";

    // get from some properties file
    private final String SERVER_ADDRESS = "152.3.53.39";
    private final int PORT = 9042;

    private final String USER_NAME_PROMPT = "Enter user name above";
    // todo - make sure no duplicate user names
    private final String USER_NAME_ACCEPTED = "Username '%s' accepted!";
    private String userName;

    private ObservableList<Node> chatItems;
    private Socket socket;
    private TextArea inputArea;
    private PrintWriter outputWriter;

    public ChatClient(Stage primaryStage) {
        chatItems = FXCollections.observableArrayList();
        ListView<Node> chatList = new ListView<>(chatItems);
        Scene chat = new Scene(chatList);
        primaryStage.setScene(chat);
        setupChatSocket();
        inputArea = new TextArea();
        inputArea.setOnKeyPressed(e -> processText(e));
        chatItems.add(chatItems.size(), inputArea);
        Text prompt = new Text(USER_NAME_PROMPT);
        prompt.setFill(Color.RED);
        chatItems.add(prompt);
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {

        }
    }

    private void processText(KeyEvent key){
        if (key.isShiftDown() && key.getCode().equals(KeyCode.ENTER)) {
            String input = inputArea.getText();
            inputArea.clear();
            if (input.trim().length() > 0) {
                try {
                    sendMessage(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMessage(String input) throws IOException {
        if (userName == null) {
            userName = input;
            Text acceptanceMessage = new Text(String.format(USER_NAME_ACCEPTED, input));
            acceptanceMessage.setFill(Color.RED);
            chatItems.add(acceptanceMessage);
        } else {
            String message = userName + MESSAGE_DELIMITER + input;
            outputWriter.println(message);
        }
    }

    private void setupChatSocket() {
        try {
            // Make connection and initialize streams
            socket = new Socket(SERVER_ADDRESS, PORT);
            outputWriter = new PrintWriter(socket.getOutputStream(), true);
            Thread t = new ChatThread(socket, chatItems);
            t.start();
        } catch (IOException socketException) {
            Text errorText = new Text(SOCKET_ERROR);
            errorText.setFill(Color.RED);
            chatItems.add(chatItems.size(), errorText);
        }
    }
}