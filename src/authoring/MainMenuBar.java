package authoring;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import display.tabs.SaveDialog;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import util.PropertiesGetter;
import display.splashScreen.ScreenDisplay;

/**
 * 
 * @author bwelton
 *
 */
public class MainMenuBar extends MenuBar{
	private static final String EXPORT = "exportGame";
	private static final String EDIT = "editGame";
	private static final String RENAME = "renameGame";
	private static final String SAVE = "saveAs";
	
	private Menu file;
	private Menu edit;
	private ScreenDisplay myDisplay;
	private AuthoringModelController myController;
	
	public MainMenuBar(ScreenDisplay display, AuthoringModelController controller) {
		this.prefWidthProperty().bind(display.getScene().widthProperty());
		myDisplay = display;
		myController = controller;
		
		createFileMenu();
		createEditMenu();
		
		this.getMenus().addAll(file, edit);
	}

	private void createEditMenu() {
		edit = new Menu(EDIT);
	}

	private void createFileMenu() {
		file = new Menu("File");
		MenuItem saveAsMenuItem = new MenuItem(SAVE);
		MenuItem exportMenuItem = new MenuItem(EXPORT);
		MenuItem renameMenuItem = new MenuItem(RENAME);
		
		saveAsMenuItem.setOnAction(ActionEvent -> saveGame());
		exportMenuItem.setOnAction(ActionEvent -> exportGame());
		renameMenuItem.setOnAction(ActionEvent -> renameGame());
		file.getItems().addAll(saveAsMenuItem, exportMenuItem, renameMenuItem);
	}
	
	private Optional<String> launchInput(String prompt) {
		TextInputDialog input = new TextInputDialog();
		input.setContentText(prompt);
		input.setGraphic(null);
		input.setTitle(null);
		input.setHeaderText(null);
		return input.showAndWait();
	}
	
	private void saveGame() {
		myDisplay.save();
	}

	private void exportGame() {
		try {
			myController.exportGame();
		} catch (IOException e) {
			// todo - get rid of this method, now called in EditDisplay
		}
	}
	
	public void renameGame() {
		Optional<String> newName = launchInput(PropertiesGetter.getProperty(RENAME));
		if(newName.isPresent()) {
			myController.setGameName(newName.get());
		}
	}
}
