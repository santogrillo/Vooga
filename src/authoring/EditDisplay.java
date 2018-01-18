package authoring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.InvalidProtocolBufferException;

import authoring.LevelToolBar.LevelToolBar;
import authoring.LevelToolBar.LevelToolBarOld;
import authoring.PropertiesToolBar.PropertiesToolBar;
import authoring.PropertiesToolBar.SpriteImage;
import authoring.customize.AttackDefenseToggle;
import authoring.customize.ColorChanger;
import authoring.customize.ThemeChanger;
import authoring.spriteTester.SpriteTesterButton;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import engine.AuthoringModelController;
import engine.PlayModelController;
import engine.authoring_engine.AuthoringController;
import engine.play_engine.PlayController;
import factory.AlertFactory;
import factory.MediaPlayerFactory;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;
import networking.protocol.AuthorClient.DefineElement;
import networking.protocol.AuthorServer.AuthoringNotification;
import networking.protocol.AuthorServer.AuthoringServerMessage;
import networking.protocol.PlayerServer;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Notification;
import player.LiveEditingPlayDisplay;
import player.PlayDisplay;
import util.DropdownFactory;
import util.Exclude;
import util.PropertiesGetter;
import util.Purger;
import util.protocol.ClientMessageUtils;
import display.splashScreen.NotifiableDisplay;
import display.splashScreen.ScreenDisplay;
import display.sprites.BackgroundObject;
import display.sprites.InteractiveObject;
import display.sprites.StaticObject;
import display.tabs.SaveDialog;
import display.toolbars.StaticObjectToolBar;

/**
 * 
 * @author bwelton, mmosca, moboyle
 *
 */
public class EditDisplay extends ScreenDisplay implements AuthorInterface, NotifiableDisplay {

	private static final String TEXT = "-";
	private static final String PLUS = "+";
	private static final String TESTING_GAME = "testingGame";
	private static final String UNTITLED = "untitled";
	private static final String ATTACK = "attackLabel";
	private static final String DEFENSE = "defenseLabel";
	private static final String DEFAULT_GAME_NAME = "temp.voog";
	private static final String DEFAULT_PATH = "authoring/";
	private static final String LOAD_GAME = "loadGameLabel";
	private static final String SAVED_GAME_LABEL = "savedGameLabel";
	private static final double GRID_X_LOCATION = 620;
	private static final double GRID_Y_LOCATION = 20;
	private final String PATH_DIRECTORY_NAME = DEFAULT_PATH;
	private final String HEIGHT = "Height";
	private final String WIDTH = "Width";

	private Scene myScene;
	private AuthoringModelController controller;
	private StaticObjectToolBar myLeftToolBar;
	private GameArea myGameArea;
	private ScrollableArea myGameEnvironment;
	private PropertiesToolBar myRightToolBar;
	private MainMenuBar myMenuBar;
	private ToggleButton gridToggle;
	private ToggleButton movementToggle;
	private ColorChanger myColorChanger;
	private ThemeChanger myThemeChanger;
	private AttackDefenseToggle myGameChooser;
	private Label attackDefenseLabel;
	private Map<String, String> basePropertyMap;
	private LevelToolBar myBottomToolBar;
	private VBox myLeftBar;
	private VBox myLeftButtonsBar;
	// private SpriteTesterButton myTesterButton;
	private Slider volumeSlider;
	private MediaPlayerFactory mediaPlayerFactory;
	private MediaPlayer mediaPlayer;
	private String backgroundSong = "data/audio/110 - pokemon center.mp3";
	private InteractiveObject objectToPlace;
	private EventHandler<MouseEvent> cursorDrag;
	private boolean addingObject = false;
	private String gameName = null;

	private DropdownFactory dropdownFactory = new DropdownFactory();

	private ClientMessageUtils clientMessageUtils;

	public EditDisplay(int width, int height, Stage stage, boolean loaded, AuthoringModelController controller) {
		super(width, height, Color.BLACK, stage);
		this.controller = controller;
		clientMessageUtils = new ClientMessageUtils();
		if (loaded) {
			loadGame();
		}
		myLeftButtonsBar = new VBox();
		myLeftBar = new VBox();
		basePropertyMap = new HashMap<>();
	}

	@Override
	public void startDisplay() {
		System.out.println("STARTING EDIT DISPLAY");
		super.startDisplay();
		addItems();
		System.out.println("Added items!");
		formatLeftBar();
		setStandardTheme();
		createGridToggle();
		createMovementToggle();
		createLabel();
		basePropertyMap = new HashMap<>();
		// Button saveButton = new Button("Save");
		// saveButton.setLayoutY(600);
		// rootAdd(saveButton);
		// myTesterButton = new SpriteTesterButton(this);
		// rootAdd(myTesterButton);
		mediaPlayerFactory = new MediaPlayerFactory(backgroundSong);
		mediaPlayer = mediaPlayerFactory.getMediaPlayer();
		if (mediaPlayer != null) {
            mediaPlayer.play();
            mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        }
		volumeSlider.setLayoutY(735);
		volumeSlider.setLayoutX(950);
		this.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> addStaticObject(e));
		myMenuBar.getMenus().clear();
		myMenuBar.getMenus().addAll(dropdownFactory.generateMenuDropdowns(this));
		System.out.println("Done starting display!");
	}

	@Override
	public void startDisplay(LevelInitialized levelData) {
		startDisplay();
		clientMessageUtils.initializeLoadedLevel(levelData);
	}

	@Override
	public void receiveNotification(byte[] messageBytes) {
		try {
			System.out.println("Receiving notification!");
			handleCommonNotifications(messageBytes);
			AuthoringServerMessage message = AuthoringServerMessage.parseFrom(messageBytes);
			AuthoringNotification notification = message.getNotification();
			if (notification.hasElementAddedToInventory()) {
				System.out.println("Someone added to inventory!");
				receiveElementAddedToInventory(notification.getElementAddedToInventory());
			}
		} catch (InvalidProtocolBufferException e) {
		}
	}

	private void handleCommonNotifications(byte[] messageBytes) {
		try {
			Notification commonNotification = Notification.parseFrom(messageBytes);
			if (commonNotification.hasElementPlaced()) {
				System.out.println("Placing element!");
				mountObjectToMap(commonNotification.getElementPlaced());
			}
			if (commonNotification.hasElementMoved()) {
				System.out.println("Moving element!");
				clientMessageUtils.updateSpriteDisplay(commonNotification.getElementMoved());
			}
			if (commonNotification.hasElementDeleted()) {
				System.out.println("Deleting element!");
				clientMessageUtils.removeDeadSpriteFromDisplay(commonNotification.getElementDeleted());
				//
				// myGameArea.objectRemoved(interactive);
			}
		} catch (InvalidProtocolBufferException e) {
		}
	}

	@Override
	public String getGameState() {
		return gameName;
	}

	public void receiveElementAddedToInventory(DefineElement elementAddedToInventory) {
		String nameOfElementAdded = elementAddedToInventory.getElementName();
		// get imageView from image name
		Optional<SpriteImage> imageViewToAdd = myRightToolBar.getImageOfAppropriateTypeFromId(nameOfElementAdded);
		if (imageViewToAdd.isPresent()) {
			myRightToolBar.imageSelected(imageViewToAdd.get());
		}
	}

	private void createGridToggle() {
		gridToggle = new ToggleButton();
		gridToggle.setLayoutX(GRID_X_LOCATION);
		gridToggle.setLayoutY(GRID_Y_LOCATION);
		gridToggle.setSelected(true);
		gridToggle
				.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("grid_icon.png"))));
		gridToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			myGameArea.toggleGridVisibility(gridToggle.isSelected());
		});
		rootAdd(gridToggle);
	}

	private void createMovementToggle() {
		movementToggle = new ToggleButton();
		movementToggle.setLayoutX(GRID_X_LOCATION - 40);
		movementToggle.setLayoutY(GRID_Y_LOCATION);
		movementToggle.setSelected(false);
		movementToggle.setGraphic(
				new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png"))));
		movementToggle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> toggleMovement(movementToggle));
		rootAdd(movementToggle);
	}

	private void toggleMovement(ToggleButton movement) {
		myGameArea.toggleMovement(movementToggle.isSelected());
		if (movement.isSelected()) {
			this.getScene().setCursor(new ImageCursor(
					new Image(getClass().getClassLoader().getResourceAsStream("scroll_arrow_icon.png")), 30, 30));
		} else {
			this.getScene().setCursor(Cursor.DEFAULT);
		}
	}

	private void createLabel() {
		attackDefenseLabel = new Label(PropertiesGetter.getProperty(DEFENSE));
		// styleLabel(attackDefenseLabel);
		attackDefenseLabel.setFont(new Font("Times New Roman", 35));
		// attackDefenseLabel.setFont(new Font("American Typewriter", 40));
		// attackDefenseLabel.setFont(new Font("Cambria", 40));
		attackDefenseLabel.setLayoutX(260);
		attackDefenseLabel.setLayoutY(25);
		rootAdd(attackDefenseLabel);
	}

	private void formatLeftBar() {
		myLeftBar.setLayoutY(30);
		myLeftBar.setSpacing(30);
		myLeftButtonsBar.setSpacing(20);
	}

	private void addItems() {
		System.out.print("Adding items");
		myGameArea = new GameArea(controller);
		myGameEnvironment = new ScrollableArea(myGameArea);
		rootAdd(myGameEnvironment);
		this.setDroppable(myGameArea);
		addToLeftBar();
		rootAdd(myLeftBar);
		myRightToolBar = new PropertiesToolBar(this, controller);
		rootAdd(myRightToolBar);
		myThemeChanger = new ThemeChanger(this);
		rootAdd(myThemeChanger);
		myMenuBar = new MainMenuBar(this, controller);
		rootAdd(myMenuBar);
		myBottomToolBar = new LevelToolBar(this, controller, myGameEnvironment);
		rootAdd(myBottomToolBar);
		volumeSlider = new Slider(0, 1, .1);
		rootAdd(volumeSlider);
	}

	private void addToLeftBar() {
		myLeftToolBar = new StaticObjectToolBar(this, controller);
		myLeftBar.getChildren().add(myLeftToolBar);
		addToLeftButtonsBar();
		myLeftBar.getChildren().add(myLeftButtonsBar);
	}

	private void addToLeftButtonsBar() {
		myColorChanger = new ColorChanger(this);
		myLeftButtonsBar.getChildren().add(myColorChanger);
		myGameChooser = new AttackDefenseToggle(this);
		myLeftButtonsBar.getChildren().add(myGameChooser);
	}

	@Override
	public void listItemClicked(MouseEvent e, ImageView clickable) {
		StaticObject object = (StaticObject) clickable;
		if(e.getButton() == MouseButton.SECONDARY) {
			Button incrementButton = new Button(PLUS);
			Button decrementButton = new Button(TEXT);
			incrementButton.setLayoutY(20);
			decrementButton.setLayoutY(20);
			incrementButton.setLayoutX(50);
			decrementButton.setLayoutX(85);
			// To-do refactor set on action if possible
			incrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				incrementObjectSize(object);
			});
			// To-do refactor set on action if possible
			decrementButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
				decrementObjectSize(object);
			});
			rootAdd(incrementButton);
			rootAdd(decrementButton);
		} else {
			if (object instanceof BackgroundObject) {
				objectToPlace = new BackgroundObject(object.getCellSize(), this, object.getElementName());
			} else {
				objectToPlace = new StaticObject(object.getCellSize(), this, object.getElementName());
			}
			rootAdd(objectToPlace);
			objectToPlace.toFront();
			cursorDrag = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					e.consume();
					objectToPlace.setX(event.getX() - objectToPlace.getFitWidth() / 2);
					objectToPlace.setY(event.getY() - objectToPlace.getFitHeight() / 2);
				}
			};
			this.getScene().addEventHandler(MouseEvent.ANY, cursorDrag);
			this.getScene().setCursor(ImageCursor.NONE);
			addingObject = true;
		}

	}

	private void decrementObjectSize(StaticObject object) {
		object.decrementSize();
		updateObjectSize(object);
	}

	private void incrementObjectSize(StaticObject object) {
		object.incrementSize();
		updateObjectSize(object);
	}

	private void updateObjectSize(StaticObject object) {
		Map<String, Object> newProperties = controller.getTemplateProperties(object.getElementName());
		newProperties.put(WIDTH, object.getSize());
		newProperties.put(HEIGHT, object.getSize());
		controller.updateElementDefinition(object.getElementName(), newProperties, false);
	}

	private void addStaticObject(MouseEvent e) {
		System.out.println("adding static object");
		if (addingObject) {
			e.consume();
			this.getScene().removeEventHandler(MouseEvent.ANY, cursorDrag);
			rootRemove(objectToPlace);
			System.out.println(objectToPlace.getElementName());
			System.out.println(controller.getAuxiliaryElementConfigurationOptions(basePropertyMap).keySet().toString());
			NewSprite newSprite = controller.placeElement(objectToPlace.getElementName(),
					new Point2D(e.getX() - objectToPlace.getFitWidth() / 2 - myGameEnvironment.getLayoutX(),
							e.getY() - objectToPlace.getFitHeight() / 2 - myGameEnvironment.getLayoutY()));
			objectToPlace.setElementId(clientMessageUtils.addNewSpriteToDisplay(newSprite));
			objectToPlace.setX(e.getX() - objectToPlace.getFitWidth() / 2 - myGameEnvironment.getLayoutX());
			objectToPlace.setY(e.getY() - objectToPlace.getFitHeight() / 2 - myGameEnvironment.getLayoutY());
			myGameArea.addBackObject(objectToPlace);
			myGameArea.droppedInto(objectToPlace);
			addingObject = false;
			System.out.println("fixing cursor");
			this.getScene().setCursor(ImageCursor.DEFAULT);
		}
	}

	private void mountObjectToMap(NewSprite newSprite) {
		System.out.println("Mounting object");
		System.out.println("X: " + newSprite.getSpawnX());
		System.out.println("Y: " + newSprite.getSpawnY());
		if (clientMessageUtils.getCurrentSpriteIds().contains(newSprite.getSpriteId())) {
			// Discard
			return;
		}
		int placedSpriteId = clientMessageUtils.addNewSpriteToDisplay(newSprite);
		objectToPlace = new InteractiveObject(this, newSprite.getImageURL());
		objectToPlace.setElementId(placedSpriteId);
		objectToPlace.setImageView(clientMessageUtils.getRepresentationFromSpriteId(placedSpriteId));
		objectToPlace.setX(newSprite.getSpawnX());
		objectToPlace.setY(newSprite.getSpawnY());

		// StaticObject newStatic = new StaticObject(objectToPlace.getCellSize(), this,
		// newSprite.getImageURL());
		myGameArea.addBackObject(objectToPlace);
		myGameArea.droppedInto(objectToPlace);
		// myGameArea.addBackObject(objectToPlace);
		// myGameArea.droppedInto(objectToPlace);
		addingObject = false;
	}

	@Override
	public void newTowerSelected(ImageView myImageView) {

	}

	@Override
	public void clicked(SpriteImage imageView) {
		SelectionWindow mySelectionWindow = new SelectionWindow(imageView, this, controller);
	}

	@Override
	public void changeColor(String color) {
		myGameArea.changeColor(color);
	}

	@Override
	public void save() {
		File saveFile = SaveDialog.SaveLocation(getScene());
		if (saveFile != null) {
			controller.setGameName(saveFile.getName());
			controller.saveGameState(saveFile.getName());
			myGameArea.savePath();
		}
	}

	// I'm adding this to do reflective generation of dropdown menu (I am Ben S)
	private void export() {
        /*Dialog dialog = new Dialog();
	    new Thread(() -> {

            dialog.setContentText("Wait for the exportation to complete...");
            dialog.show();
        }).start();
		final String[] DIALOG_MESSAGE = new String[1];
		Task<String> exportTask = new Task<String>() {
			@Override
			protected String call() throws Exception {
				DIALOG_MESSAGE[0] = controller.exportGame();
				return controller.exportGame();
			}
		};
		exportTask.setOnSucceeded(event -> dialog.close());
		try {
			Thread run = new Thread(exportTask);
			Platform.runLater(run);
		} catch (Exception e) {
			DIALOG_MESSAGE[0] = e.getMessage();
		}*/
        String[] DIALOG_MESSAGE = new String[1];
		    try {
                DIALOG_MESSAGE[0] = controller.exportGame();
            } catch (IOException e){
		        DIALOG_MESSAGE[0] = e.getMessage();
            } finally {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("Share your game with this link");
                alert.setTitle("Export Link");
                TextArea copyable = new TextArea(DIALOG_MESSAGE[0]);
                copyable.setEditable(false);
                DialogPane dialogPane = new DialogPane();
                dialogPane.setContent(copyable);
                alert.setGraphic(copyable);
                alert.showAndWait().filter(press -> press == ButtonType.OK)
                        .ifPresent(event -> alert.close());
            }
	}

	private void rename() {
		myMenuBar.renameGame();
	}

	private void addWave() {
		myBottomToolBar.makeNewWave();
	}

	private void addLevel() {
		myBottomToolBar.addLevel();
	}

	private void editLevel() {
		myBottomToolBar.openLevelDisplay();
	}

	private void playGame() {
//	    final String AUTHORING = DEFAULT_PATH;
//	    final String GAME_NAME = DEFAULT_GAME_NAME;
        myGameArea.savePath();
        controller.setGameName(DEFAULT_GAME_NAME);
        controller.saveGameState(DEFAULT_GAME_NAME);
        PlayModelController playModelController = new PlayController();
        try {
            playModelController.loadOriginalGameState(DEFAULT_GAME_NAME, 1);
            LiveEditingPlayDisplay playDisplay =
                    new LiveEditingPlayDisplay(PLAYWIDTH, PLAYHEIGHT, getStage(), new PlayController());
            playDisplay.launchGame(DEFAULT_GAME_NAME);
            myScene = this.getScene();
            getStage().setScene(playDisplay.getScene());
            getStage().setOnCloseRequest(e->{
            	e.consume();
            	returnToEdit();});
        } catch (Exception e) {
            new AlertFactory(e.getMessage(),AlertType.ERROR);
        } finally {
            new Purger().purge();
        }
    }
	
	private void returnToEdit() {
		getStage().setScene(myScene);
		getStage().setOnCloseRequest(null);
	}

	// end

	private void loadGame() {
		List<String> games = new ArrayList<>();
		for (String title : controller.getAvailableGames().keySet()) {
			games.add(title);
		}
		Collections.sort(games);
		ChoiceDialog<String> loadChoices = new ChoiceDialog<>(PropertiesGetter.getProperty(SAVED_GAME_LABEL), games);
		loadChoices.setTitle(PropertiesGetter.getProperty(LOAD_GAME));
		loadChoices.setContentText(null);

		Optional<String> result = loadChoices.showAndWait();
		if (result.isPresent()) {
			try {
				gameName = result.get();
				System.out.println("Set game name to " + gameName);
				clientMessageUtils.initializeLoadedLevel(controller.loadOriginalGameState(gameName, 1));
			} catch (IOException e) {
				// TODO Change to alert for the user
				e.printStackTrace();
			}
		} else {
			returnButtonPressed();
		}
	}

	public void changeTheme(String theme) {
		rootStyleAndClear(myThemeChanger.getThemePath(theme));
		myRightToolBar.getStyleClass().add("borders");
		myLeftToolBar.getStyleClass().add("borders");
		myLeftBar.getStyleClass().add("outer-border");
		myLeftButtonsBar.getStyleClass().add("borders");
	}

	private void setStandardTheme() {
		changeTheme(ThemeChanger.STANDARD);
	}

	public void attack() {
		attackDefenseLabel.setText(PropertiesGetter.getProperty(DEFENSE));
	}

	public void defense() {
		attackDefenseLabel.setText(PropertiesGetter.getProperty(ATTACK));
	}

	public void submit(String levelAndWave, String location, int amount, ImageView mySprite) {
		myBottomToolBar.addToWave(levelAndWave, location, amount, mySprite);
	}

	@Override
	public String[] getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnButtonPressed() {
		if (!controller.getGameName().equals(UNTITLED)) {
			controller.saveGameState(new File(controller.getGameName()).getName());
		} else {
			this.save();
		}
		mediaPlayer.stop();
		VBox newProject = new VBox();
		Scene newScene = new Scene(newProject, 400, 400);
		Stage myStage = new Stage();
		myStage.setScene(newScene);
		myStage.show();
		Main restart = new Main();
		restart.start(myStage);
		getStage().close();
	}

	@Override
	public void imageSelected(SpriteImage imageView) {
		imageView.setBaseProperties(basePropertyMap);
		imageView.createInitialProperties(controller.getAuxiliaryElementConfigurationOptions(basePropertyMap));
		controller.defineElement(imageView.getId(), imageView.getAllProperties());
		controller.setUnitCost(imageView.getId(), new HashMap<>());
		controller.addElementToInventory(imageView.getId());
		myRightToolBar.imageSelected(imageView);
	}

	@Override
	public void addToMap(String baseProperty, String value) {
		basePropertyMap.put(baseProperty, value);
		// myRightToolBar.addToMap(baseProperty, value);
	}

	public void setGameArea(GameArea game) {
		this.myGameArea = game;
	}

	@Override
	public void createTesterLevel(Map<String, Object> fun, List<String> sprites) {
		// TODO - Update this method accordingly to determine the isMultiPlayer param
		// for PlayDisplay constructor
		PlayDisplay testingScene = new PlayDisplay(1000, 1000, getStage(), new PlayController()); // TEMP
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		getStage().setX(primaryScreenBounds.getWidth() / 2 - 1000 / 2);
		getStage().setY(primaryScreenBounds.getHeight() / 2 - 1000 / 2);
		getStage().setScene(testingScene.getScene());
		controller.setGameName(TESTING_GAME);
		//try {
			controller.createWaveProperties(fun, sprites, new Point2D(100, 100));
		/*} catch (ReflectiveOperationException failedToGenerateWaveException) {
			// todo - handle
		}*/
	}

	public void addToBottomToolBar(int level, ImageView currSprite, int kind) {
		if (kind == 1) {
			// myBottomToolBar.addToWave(currSprite, level, 3);
		}
		if (kind == 2) {
			myBottomToolBar.addLevelProperties(currSprite, level);
		}
	}

}
