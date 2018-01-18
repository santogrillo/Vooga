package display.tabs;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import display.interfaces.ClickableInterface;
import javafx.scene.control.TabPane;
import display.splashScreen.ScreenDisplay;
import display.sprites.StaticObject;

/**
 * 
 * @author bwelton
 *
 */
public class AddStaticTab extends AddTab {
	private final int STARTING_SIZE = 1;
	private final String PACKAGE = "sprites.";
	
	public AddStaticTab(ScreenDisplay display, TabPane tabs) {
		super(display, tabs);
	}

	@Override
	protected void createResource(File file, String tabName) {
		SimpleTab activeTab = (SimpleTab) tabPane.getTabs().get(objectTypes.getSelectionModel().getSelectedIndex()).getContent();
		
		try {
			Class<?> clazz = Class.forName(PACKAGE + tabName);
			Constructor<?> ctor = clazz.getDeclaredConstructor(int.class, ClickableInterface.class, String.class);
			StaticObject object = (StaticObject) ctor.newInstance(STARTING_SIZE, myDisplay, file.toURI().toString());
			activeTab.addItem(object);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException |
				IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//Add in error handling here
			e.printStackTrace();
		}
	}

}
