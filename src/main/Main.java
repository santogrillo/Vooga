package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import display.splashScreen.ScreenDisplay;
import display.splashScreen.SplashScreen;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import util.PropertiesGetter;

public class Main extends Application {

	public static final String TITLE = "SplashTitle";
	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int FRAMES_PER_SECOND = 10;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start(Stage s) {
		ScreenDisplay myScene = new SplashScreen(WIDTH, HEIGHT, BACKGROUND, s);
		s.setScene(myScene.getScene());
		String title = PropertiesGetter.getProperty(TITLE);
		s.setTitle(title);
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
