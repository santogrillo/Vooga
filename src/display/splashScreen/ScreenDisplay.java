package display.splashScreen;

import java.io.File;

import display.interfaces.Droppable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import networking.protocol.PlayerServer.LevelInitialized;

public abstract class ScreenDisplay {

    protected static final int PLAYWIDTH = 1000;
    protected static final int PLAYHEIGHT = 700;
    public static final double FRAMES_PER_SECOND = 60;
	public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 100.0 / FRAMES_PER_SECOND;
	private Droppable droppable;
	private KeyFrame frame;
	private Timeline animation = new Timeline();
	protected Scene myScene;
	private Stage stage;
	private Group root = new Group();

	/**
	 * Constructor: Screen Display class
	 * @param currentStage 
	 */

	public ScreenDisplay(int width, int height, Paint background, Stage currentStage) {
		stage = currentStage;
		setMyScene(new Scene(root, width, height, background));
	}
	
	public ScreenDisplay(int width, int height) {
		init();
		setMyScene(new Scene(root, width, height));

	}
	
	public ObservableList<Node> getRootChildren() {
		return root.getChildren();
	}
	
	public void init() {
		//frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> this.step(SECOND_DELAY));
		animation.setCycleCount(Timeline.INDEFINITE);
		//animation.getKeyFrames().add(frame);
	}
	
	protected void rootAdd(Node object) {
		root.getChildren().add(object);
	}

	protected void rootRemove(Node object) {
		root.getChildren().remove(object);
	}
	
	protected boolean rootContain(Node object) {
		return root.getChildren().contains(object);
	}
	
	protected void rootStyleAndClear(String sheet) {
		root.getStylesheets().clear();
		root.getStylesheets().add(sheet);
	}
	
	protected void rootStyle(String sheet) {
		root.getStylesheets().add(sheet);
	}
	
	protected Stage getStage() {
		return stage;
	}
	
	public Droppable getDroppable() {
		return droppable;
	}
	
	public void setDroppable(Droppable drop) {
		droppable = drop;
	}
	
	public abstract void save();
	
	public abstract void listItemClicked(MouseEvent e, ImageView object);

	public Scene getScene() {
		return myScene;
	}
	
	public void setMyScene(Scene myScene) {
		this.myScene = myScene;
	}

	public void startDisplay() {
		animation.play();
	}
}
