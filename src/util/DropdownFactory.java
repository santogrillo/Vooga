package util;

import display.splashScreen.ScreenDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Used to generate the IDE menu bar items from a properties file.
 *
 * @author Ben Schwennesen
 */
public class DropdownFactory {

    private final String INFO_DELIMITER = ",";
    private final String PROPERTIES_FILE = "Dropdown.properties";
    private final int INFO_LENGTH = 2;

    private Properties menuProperties = new LinkedProperties();

    public DropdownFactory() {
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            menuProperties.load(in);
        } catch (IOException | NullPointerException e) {
            // do nothing
        }
    }

    private void generateMenuItem(Map<String, Menu> dropdownsMap, String itemName, ScreenDisplay display) {
        String[] dropdownInfo;
        if (menuProperties.containsKey(itemName)) {
            dropdownInfo = menuProperties.getProperty(itemName).split(INFO_DELIMITER);
            if (dropdownInfo.length != INFO_LENGTH) {
                return;
            }
            String dropdownName = dropdownInfo[0];
            Menu dropdown = dropdownsMap.getOrDefault(dropdownName, new Menu(dropdownName));
            MenuItem menuItem = new MenuItem(itemName);
            try {
                Method actionMethod = display.getClass().getDeclaredMethod(dropdownInfo[1]);
                menuItem.setOnAction(e -> runMenuAction(actionMethod, display));
            } catch (ReflectiveOperationException failedToLoadEventRunnerException) {
                failedToLoadEventRunnerException.printStackTrace();
            }
            dropdown.getItems().add(menuItem);
            dropdownsMap.put(dropdownName, dropdown);
        }
    }

    private void runMenuAction(Method actionMethod, Object runner) {
        try {
            actionMethod.setAccessible(true);
            System.out.println(actionMethod.getName());
            actionMethod.invoke(runner);
        } catch (ReflectiveOperationException methodFailedToRunException) {
            methodFailedToRunException.printStackTrace();
            // todo - handle
        }

    }

    /**
     * Retrieve the dropdown items for use in the IDE menu bar, as generated from the properties file.
     *
     * @return a list of dropdown items to be included in the IDE menu bar
     */
    public List<Menu> generateMenuDropdowns(ScreenDisplay display) {
        Map<String, Menu> dropdownsMap = new LinkedHashMap<>();
        for (String itemName : menuProperties.stringPropertyNames()) {
            generateMenuItem(dropdownsMap, itemName, display);
        }
        return new ArrayList<>(dropdownsMap.values());
    }
}