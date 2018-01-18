package networking;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * For testing purposes. Eventually chat area will be part of authoring/game window.
 *
 * @author Ben Schwennesen
 */
public class ChatWindow extends Application {

    ChatClient testClient;

    @Override
    public void start(Stage primaryStage) {
        this.testClient = new ChatClient(primaryStage);
        primaryStage.setTitle("CHAT");
        primaryStage.show();
    }

    @Override
    public void stop() {
        testClient.closeSocket();
    }

    public static void main(String[] args) { launch(args); }
}
