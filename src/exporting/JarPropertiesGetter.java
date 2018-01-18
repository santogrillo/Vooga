package exporting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Utility class for retrieving game export file's properties, such as the location of the source directory.
 *
 * @author Ben Schwennesen
 */
public final class JarPropertiesGetter {

    // Relocate the properties file as fits your needs
    private final String PROPERTIES_FILE = "Export.properties";
    private Properties properties;
    // Property keys
    private final String EXPORT_PATH_KEY = "export-path";
    private final String SOURCE_DIRECTORY_KEY = "source";
    private final String MAIN_CLASS_NAME_KEY = "main-class";
    private final String INCLUDED_DIRECTORIES_KEY = "included-directories";
    private final String RESOURCE_ROOTS_KEY = "resource-roots";
    private final String DATA_DIRECTORIES_KEY = "data-directories";
    private final String DISPLAYED_GAME_NAME_KEY = "displayed-game-name";
    private final String MULTIPLE_VALUES_DELIMITER = ",";
    // Set to your heart's desire
    private final String DEFAULT = "";
    private final String ERROR_MESSAGE = "Failed to load the export properties file";
    private final String RESOURCES_DIRECTORY = "resources/";
    private final String VOOG_EXTENSION = ".voog";

    private final OutputStream PROPERTIES_OUTPUT;

    /**
     * Load in the properties file.
     */
    public JarPropertiesGetter() throws IOException {
        properties = new Properties();
        InputStream propertiesStream = JarPropertiesGetter.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        PROPERTIES_OUTPUT = new FileOutputStream(RESOURCES_DIRECTORY + PROPERTIES_FILE);
        try {
            properties.load(propertiesStream);
        } catch (NullPointerException | IOException failure) {
            throw new IOException(ERROR_MESSAGE);
        }
    }

    /**
     * Get the target path for a game's JAR export file.
     *
     * @param gameName the name given to the game
     * @return the path of the export JAR (relative to the project root)
     */
    public String getExportTargetPath(String gameName) {
        final String JAR_EXTENSION = ".jar";
        return properties.getProperty(EXPORT_PATH_KEY, DEFAULT)
                + properties.getProperty(DISPLAYED_GAME_NAME_KEY, gameName) + JAR_EXTENSION;
    }

    /**
     * Get the source directory path.
     *
     * @return the relative path to the project's source folder
     */
    public String getSourceDirectoryPath() {
        return properties.getProperty(SOURCE_DIRECTORY_KEY, DEFAULT);
    }

    /**
     * Get the name of the launching class.
     *
     * @return the name of the class used to launch the exported JAR
     */
    public String getMainClassFullName() {
        return properties.getProperty(MAIN_CLASS_NAME_KEY, DEFAULT);
    }

    /**
     * Get source directories that should be included in the export JAR. For example, this might include only engine
     * and player modules. By default--that is, if the property is left unset--this will include all source directories.
     *
     * @return the specified directories within the source that should be included in the exported JAR file
     */
    public Collection<String> getSourceDirectoriesToInclude() {
        return Arrays.asList(properties.getProperty(INCLUDED_DIRECTORIES_KEY, getSourceDirectoryPath())
                .split(MULTIPLE_VALUES_DELIMITER));
    }

    /**
     * Get resource roots for the export. This includes any directory which has been marked as a resource root in
     * the development environment used to write the project. Files and directories within these roots will be
     * included in the root of the JAR, allowing calls to getClass().getResourceAsRoot() to function as expected.
     *
     * @return all specified resource roots for the project needed to run the exported JAR
     */
    public Collection<String> getResourceRoots() {
        return Arrays.asList(properties.getProperty(RESOURCE_ROOTS_KEY, DEFAULT).split(MULTIPLE_VALUES_DELIMITER));
    }

    /**
     * Get data and library directories for the export. This includes any directories directly referred to by their
     * full paths relative to the project root directory.
     *
     * @return all specified data directories needed to run the exported JAR
     */
    public Collection<String> getDataAndLibraryDirectories() {
        return Arrays.asList(properties.getProperty(DATA_DIRECTORIES_KEY, DEFAULT).split(MULTIPLE_VALUES_DELIMITER));
    }

    /**
     * Set the name of the game displayed in the splash screen for the main class of the JAR when it's run.
     *
     * @param gameName the name of the particular authored game
     */
    public void setDisplayedGameName(String gameName) {
        if (gameName.contains(VOOG_EXTENSION)) {
            properties.setProperty(DISPLAYED_GAME_NAME_KEY, gameName.substring(0, gameName.indexOf(VOOG_EXTENSION)));

        } else {
            properties.setProperty(DISPLAYED_GAME_NAME_KEY, gameName);

        }
        try {
            properties.store(PROPERTIES_OUTPUT, "");
        } catch (IOException failedToExportProperties) {
            // leave the property as default
            // this should not occur though since we were able to access the file via an input stream
        }
    }
}