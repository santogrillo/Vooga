package display.tabs;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import authoring.PropertiesToolBar.SpriteImage;
import javafx.scene.control.TabPane;
import display.splashScreen.ScreenDisplay;

/**
 * 
 * @author bwelton
 *
 */
public class AddSpriteImageTab extends AddTab{
	private final String PACKAGE = "authoring.rightToolBar.";

	public AddSpriteImageTab(ScreenDisplay display, TabPane tabs) {
		super(display, tabs);
	}

	@Override
	protected void createResource(File file, String tabName) {
		NewSpriteTab activeTab = (NewSpriteTab) tabPane.getTabs().get(objectTypes.getSelectionModel().getSelectedIndex()).getContent();
		
		try {
			Class<?> clazz = Class.forName(PACKAGE + tabName);
			Constructor<?> ctor = clazz.getDeclaredConstructor(String.class);
			SpriteImage object = (SpriteImage) ctor.newInstance(file.toURI().toString());
			activeTab.addImage(object);
		} catch (ClassNotFoundException | SecurityException | InstantiationException |
				IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
			//Add in error handling here
			e.printStackTrace();
		}
	}

}
