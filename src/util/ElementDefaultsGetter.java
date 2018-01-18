package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Get the default properties of an element from a specified resource file.
 *
 * @author Ben Schwennesen
 */
public class ElementDefaultsGetter {

    private ResourceBundle properties;
    private final String DEFAULTS_RESOURCE_DIRECTORY = "defaults/";
    private final String LIST_START = "\\[", LIST_END = "]", DELIMITER = ",";

    /**
     * Create a getter to obtain the default properties for a game element.
     *
     * @param bundleName the path to the element type's default properties file
     */
    public ElementDefaultsGetter(String bundleName) {
        properties = ResourceBundle.getBundle(DEFAULTS_RESOURCE_DIRECTORY + bundleName);
    }

    /**
     * Get the default properties.
     *
     * @return a map of the element's default properties
     */
    public Map<String, Object> getDefaultProperties() {
        Map<String, Object> defaultProperties = new HashMap<>();
        for (String keyName : properties.keySet()) {
            defaultProperties.put(keyName, convertPropertyStringToObject(properties.getString(keyName)));
        }
        return defaultProperties;
    }

    private Object convertPropertyStringToObject(String propertyString) {
        if (propertyString.startsWith(LIST_START) && propertyString.endsWith(LIST_END)) {
            return parseList(propertyString);
        }
        try {
            return Integer.parseInt(propertyString);
        } catch (NumberFormatException nonDoublePropertyException) {
            try {
                return Double.parseDouble(propertyString);
            } catch (NumberFormatException nonIntegerPropertyException) {
                return propertyString;
            }
        }
    }

    private List<String> parseList(String listString) {
        listString.replaceFirst(LIST_START, "");
        listString = listString.substring(0, listString.lastIndexOf(LIST_END));
        return Arrays.asList(listString.split(DELIMITER));
    }
}
