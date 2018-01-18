package main;

import display.splashScreen.ScreenDisplay;
import display.splashScreen.SplashPlayScreen;
import display.splashScreen.SplashScreen;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class MainJarRunner extends Application {

	public static final String TITLE = "Duvall In This Together";
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start(Stage s) {
		ScreenDisplay myScene = new SplashPlayScreen(WIDTH, HEIGHT, BACKGROUND, s);
		s.setScene(myScene.getScene());
		s.setTitle(TITLE);
		s.setResizable(false);
		s.show();
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
