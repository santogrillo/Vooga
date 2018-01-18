package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.scene.paint.Color;

/**
 * Static utility class for retrieving information from properties files. All methods are static and fields are
 * initialized ina static block.
 * 
 *
 * @author Venkat Subramaniam
 */
public final class PropertiesGetter {

    private static String[] PROPERTIES_FILES;
    private static final String[] ENGLISH_PROPERTIES_FILES = {"authoring/resources/EnglishText","player/resources/EnglishText"};
    private static final String[] FRENCH_PROPERTIES_FILES = {"authoring/resources/FrenchText","player/resources/FrenchText"};
    private static final String ENGLISH = "English";
//    private static final Properties PROPERTIES;
    private static Map<String, String> MYMAP;
	private static final Object FRENCH = "French";
	private static boolean languageSet;

    /**
     * Blank, private constructor to ensure no other class tries to create an instance of this
     * utility class.
     */
    private PropertiesGetter() {
        // do nothing
    }

    /** Use static block to initialize the static java.util.Properties member */
    static {
//        PROPERTIES = new Properties();
    		MYMAP = new HashMap<>();
    		setLanguageFiles(ENGLISH);
        setup();
    }
    
    private static void setup() {
    	try {
            for (String propertiesFile : PROPERTIES_FILES) {
//                InputStream propertiesStream = PropertiesGetter.class.getClassLoader()
//                        .getResourceAsStream(propertiesFile);
//                Properties properties = new Properties();
//                properties.load(propertiesStream);
//                PROPERTIES.putAll(properties);
            		ResourceBundle rb = ResourceBundle.getBundle(propertiesFile);
            		Enumeration<String> keys = rb.getKeys();
            		while(keys.hasMoreElements()) {
            			String current = keys.nextElement();
            			if(!languageSet) {
            				languageSet = true;
            			if (MYMAP.containsKey(current)) {
            				throw new Exception();
            			}
            			
            		}
            			MYMAP.put(current, rb.getString(current));
            		}
            }
        } catch (Exception e) {
            /* do nothing: if file fails to load, all methods are prepared to return
             * default/fallback value when getProperty() returns null */
        }
    }

    /**
     * Get a property that is know to be a string.
     *
     * @param key  the key used to index the desired configuration value
     * @return value  the string configuration value we want to get
     */
    public static String getProperty(String key) {
        return MYMAP.get(key);
    }

    public static void setLanguageFiles(String language) {
		if (language.equals(ENGLISH)) {
    			PROPERTIES_FILES = ENGLISH_PROPERTIES_FILES;
		}
		if (language.equals(FRENCH)) {
			PROPERTIES_FILES = FRENCH_PROPERTIES_FILES;
		}
		setup();
	}

	/**
     * Get a property that is know to be an integer.
     *
     * @param key the key used to index the desired configuration value
     * @return value the integer configuration value we want to get
     */
    public static int getIntegerProperty(String key) {
        String value = MYMAP.get(key);
//         if the key is not found, Properties will return null and we should return a default value
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    /**
     * Get a property that is know to be a double.
     *
     * @param key  the key used to index the desired configuration value
     * @return value the double configuration value we want to get
     */
    public static double getDoubleProperty(String key) {
        String value = MYMAP.get(key);
        // if the key is not found, Properties will return null and we should return a default value
        if (value == null) {
            return 0;
        }
        return Double.parseDouble(value);
    }
}