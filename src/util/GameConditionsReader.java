package util;

import java.util.List;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.stream.Collectors;

public class GameConditionsReader {

	private final String VICTORY_CONDITIONS_PROPERTIES_FILE = "VictoryConditions.properties";
	private final String DEFEAT_CONDITIONS_PROPERTIES_FILE = "DefeatConditions.properties";
	private final Properties VICTORY_CONDITIONS_PROPERTIES;
	private final Properties DEFEAT_CONDITIONS_PROPERTIES;
	private final String READABLE_REGEX = " ";
	private final String WRITABLE_REGEX = "_";

	public GameConditionsReader() {
		VICTORY_CONDITIONS_PROPERTIES = new Properties();
		DEFEAT_CONDITIONS_PROPERTIES = new Properties();
		try {
			InputStream victoryConditionsPropertiesStream = getClass().getClassLoader()
					.getResourceAsStream(VICTORY_CONDITIONS_PROPERTIES_FILE);
			VICTORY_CONDITIONS_PROPERTIES.load(victoryConditionsPropertiesStream);
			InputStream defeatConditionsPropertiesStream = getClass().getClassLoader()
					.getResourceAsStream(DEFEAT_CONDITIONS_PROPERTIES_FILE);
			DEFEAT_CONDITIONS_PROPERTIES.load(defeatConditionsPropertiesStream);

		} catch (IOException e) {
		}
	}

	public Collection<String> getPossibleVictoryConditions() {
		return getReadablePropertiesFromRawProperties(VICTORY_CONDITIONS_PROPERTIES);
	}

	public Collection<String> getPossibleDefeatConditions() {
		return getReadablePropertiesFromRawProperties(DEFEAT_CONDITIONS_PROPERTIES);
	}

	public String getMethodNameForVictoryCondition(String condition) {
		return VICTORY_CONDITIONS_PROPERTIES.getProperty(condition.replaceAll(READABLE_REGEX, WRITABLE_REGEX));
	}

	public String getMethodNameForDefeatCondition(String condition) {
		return DEFEAT_CONDITIONS_PROPERTIES.getProperty(condition.replaceAll(READABLE_REGEX, WRITABLE_REGEX));
	}

	private List<String> getReadablePropertiesFromRawProperties(Properties properties) {
		return properties.stringPropertyNames().parallelStream()
				.map(rawPropertyName -> rawPropertyName.replaceAll(WRITABLE_REGEX, READABLE_REGEX))
				.collect(Collectors.toList());
	}

}
