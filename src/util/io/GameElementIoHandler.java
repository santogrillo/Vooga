package util.io;

import javafx.geometry.Point2D;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Exports the game elements defined in a game to properties files.
 *
 * @author Ben Schwennesen
 */
public class GameElementIoHandler {

    private final String PROPERTIES_COMMENT = "Programmatically generated game element template file";
    private final String TEMPLATE_FILE_OUTPUT_PATH = "authoring/sprite-templates/";
    private final String WAVES_OUTPUT_PATH = "authoring/waves/";
    private final String PROPERTIES_EXTENSION = ".properties";
    private final String UPGRADE_INDICATOR = "_upgrade_";
    private final String LEVEL_FILE_INDICATOR = "level_";
    private final String DELIMITER = ",";
    private final char DOT = '.';

    private SerializationUtils serializationUtils;

    public GameElementIoHandler(SerializationUtils serializationUtils) {
        this.serializationUtils = serializationUtils;
    }

    /**
     * Export all the stored sprite templates for an authored game to properties files.
     *
     * @param gameName        the name of the authored game
     * @param spriteTemplates the sprite templates defined in the game
     */
    public void exportElementTemplates(String gameName, Map<String, Map<String, Object>> spriteTemplates) {
        String directoryPath = createDirectory(gameName, TEMPLATE_FILE_OUTPUT_PATH);
        for (String templateName : spriteTemplates.keySet()) {
            Properties templateProperties = new Properties();
            Map<String, ?> templatePropertiesMap = spriteTemplates.get(templateName);
            Map<String, String> serializedPropertiesMap =
                    serializationUtils.serializeElementTemplate(templatePropertiesMap);
            serializedPropertiesMap.forEach(templateProperties::setProperty);
            String fileName = templateName + PROPERTIES_EXTENSION;
            File exportFile = new File(directoryPath + File.separator + fileName);
            writeTemplateToFile(templateProperties, exportFile);
        }
    }


    /**
     * Export game element upgrade properties for an authored game.
     *
     * @param gameName       the name of the authored game
     * @param spriteUpgrades the sprite upgrades defined in the game, associated with sprite templates
     */
    public void exportElementUpgrades(String gameName, Map<String, List<Map<String, Object>>> spriteUpgrades) {
        Map<String, Map<String, Object>> upgradeTemplates = new HashMap<>();
        for (String templateName : spriteUpgrades.keySet()) {
            List<Map<String, Object>> upgradesForTemplate = spriteUpgrades.get(templateName);
            for (int upgradeLevel = 0; upgradeLevel < upgradesForTemplate.size(); upgradeLevel++) {
                String upgradeFileName = templateName + UPGRADE_INDICATOR + upgradeLevel;
                upgradeTemplates.put(upgradeFileName, upgradesForTemplate.get(upgradeLevel));
            }
        }
        exportElementTemplates(gameName, upgradeTemplates);
    }

    /**
     * Export the waves elements for an authored game.
     *
     * @param gameName   the name of the authored game
     * @param levelWaves the waves in each level of the game
     */
    public void exportWaves(String gameName, List<Map<String, Point2D>> levelWaves) {
    	System.out.println("exporting waves");
    	System.out.println(levelWaves.toString());
        String directoryPath = createDirectory(gameName, WAVES_OUTPUT_PATH);
        for (int level = 1; level < levelWaves.size(); level++) {
        	System.out.println("Exporting Wave " + level);
            String fileName = LEVEL_FILE_INDICATOR + level + PROPERTIES_EXTENSION;
            Map<String, Point2D> wavesInLevel = levelWaves.get(level);
            Properties levelProperties = new Properties();
            for (String waveTemplate : wavesInLevel.keySet()) {
                Point2D waveSpawnLocation = wavesInLevel.get(waveTemplate);
                String waveSpawnLocationAsString = waveSpawnLocation.getX() + DELIMITER + waveSpawnLocation.getY();
                levelProperties.setProperty(waveTemplate, waveSpawnLocationAsString);
                File exportFile = new File(directoryPath + File.separator + fileName);
                writeTemplateToFile(levelProperties, exportFile);
            }
        }
    }

    /**
     * Import all the stored sprite templates for an authored game.
     *
     * @param gameName the name of the authored game
     * @return the sprite templates defined in the game in a map
     * @throws IOException if data files for the game are corrupted
     */
    public Map<String, Map<String, Object>> loadElementTemplates(String gameName) throws IOException {
        Map<String, Map<String, String>> spriteTemplatesSerializations = loadTemplateSerializations(gameName, false);
        Map<String, Map<String, Object>> spriteTemplates = new HashMap<>();
        for (String templateName : spriteTemplatesSerializations.keySet()) {
            Map<String, String> spriteTemplateSerialization = spriteTemplatesSerializations.get(templateName);
            Map<String, Object> spriteTemplate =
                    serializationUtils.deserializeElementTemplate(spriteTemplateSerialization);
            spriteTemplates.put(templateName, spriteTemplate);
        }
        return spriteTemplates;
    }


    /**
     * Import all the stored element upgrades for all element templates for an authored game.
     *
     * @param gameName the name of the authored game
     * @return the upgrades defined for each element template defined in the game, in a map
     * @throws IOException if data files for the game are corrupted
     */
    public Map<String, List<Map<String, Object>>> loadElementUpgrades(String gameName) throws IOException {
        Map<String, Map<String, String>> serializedTemplates = new TreeMap<>(loadTemplateSerializations(gameName, true));
        Map<String, List<Map<String, Object>>> spriteUpgrades = new HashMap<>();
        for (String upgradeTemplateName : serializedTemplates.keySet()) {
            String templateName = upgradeTemplateName.substring(0, upgradeTemplateName.indexOf(UPGRADE_INDICATOR));
            Map<String, Object> upgradeTemplate =
                    serializationUtils.deserializeElementTemplate(serializedTemplates.get(upgradeTemplateName));
            // TreeMap ensures correct ordering
            List<Map<String, Object>> upgrades = spriteUpgrades.getOrDefault(templateName, new ArrayList<>());
            upgrades.add(upgradeTemplate);
            spriteUpgrades.put(templateName, upgrades);
        }
        return spriteUpgrades;
    }


    /**
     * Import all the waves in each level of an authored game.
     *
     * @param gameName the name of the authored game
     * @return the waves defined in each level of the game, in a list
     * @throws IOException if data files for the game are corrupted
     */
    public List<Map<String, Point2D>> loadWaves(String gameName) throws IOException {
        List<Map<String, Point2D>> levelWaves = new ArrayList<>();
        String directoryPath = createDirectoryPath(gameName, WAVES_OUTPUT_PATH);
        File wavesDirectory = new File(directoryPath);
        File[] wavePropertiesFiles = wavesDirectory.listFiles();
        if (wavePropertiesFiles != null) {
            Arrays.sort(wavePropertiesFiles);
            for (File wavePropertiesFile : wavePropertiesFiles) {
                processLevelWaveFile(levelWaves, wavePropertiesFile);
            }
        } else {
            loadWavesInsideJar(levelWaves, directoryPath);
        }
        return levelWaves;
    }

    private void loadWavesInsideJar(List<Map<String, Point2D>> levelWaves, String directoryPath) throws IOException {
        Map<String, Map<String, String>> levelWavesInfo = new HashMap<>();
        loadTemplatesInsideJar(false, levelWavesInfo, directoryPath);
        List<String> sortedLevelIndicators = new ArrayList<>(levelWavesInfo.keySet());
        Collections.sort(sortedLevelIndicators);
        for (String levelIndicator : sortedLevelIndicators) {
            Map<String, String> wavesInLevelStrings = levelWavesInfo.get(levelIndicator);
            Map<String, Point2D> wavesInLevel = new HashMap<>();
            wavesInLevelStrings.forEach((templateName, locationAsString) ->
                    wavesInLevel.put(templateName, extractWaveSpawnLocation(locationAsString)));
            levelWaves.add(wavesInLevel);
        }
    }

    private void processLevelWaveFile(List<Map<String, Point2D>> levelWaves, File wavePropertiesFile)
            throws IOException {
        Properties waveProperties = new Properties();
        waveProperties.load(new FileInputStream(wavePropertiesFile));
        Map<String, Point2D> waveMap = new HashMap<>();
        for (String waveTemplateName : waveProperties.stringPropertyNames()) {
            String coodinatesString = waveProperties.getProperty(waveTemplateName);
            Point2D spawnLocation = extractWaveSpawnLocation(coodinatesString);
            waveMap.put(waveTemplateName, spawnLocation);
        }
        levelWaves.add(waveMap);
    }

    private Point2D extractWaveSpawnLocation(String coodinatesString) {
        String[] coordinates = coodinatesString.split(DELIMITER);
        double x = Double.parseDouble(coordinates[0].trim()), y = Double.parseDouble(coordinates[1].trim());
        return new Point2D(x, y);

    }

    private Map<String, Map<String, String>> loadTemplateSerializations(String gameName, boolean loadUpgrades)
            throws IOException {
        Map<String, Map<String, String>> spriteTemplates = new TreeMap<>();
        String directoryPath = createDirectoryPath(gameName, TEMPLATE_FILE_OUTPUT_PATH);
        File propertiesDirectory = new File(directoryPath);
        File[] spritePropertiesFiles = propertiesDirectory.listFiles();
        if (spritePropertiesFiles != null) {
            for (File spritePropertiesFile : spritePropertiesFiles) {
                if (spritePropertiesFile.getPath().endsWith(PROPERTIES_EXTENSION)
                        && loadUpgrades == spritePropertiesFile.getPath().contains(UPGRADE_INDICATOR)) {
                    loadTemplateProperties(spriteTemplates, spritePropertiesFile.getName(),
                            new FileInputStream(spritePropertiesFile));
                }
            }
        } else {
            loadTemplatesInsideJar(loadUpgrades, spriteTemplates, directoryPath);
        }
        return spriteTemplates;
    }

    private void loadTemplatesInsideJar(boolean loadUpgrades, Map<String, Map<String, String>> spriteTemplates,
                                        String directoryPath) throws IOException {
        CodeSource src = getClass().getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipFile zipFile = new ZipFile(jar.getPath());
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.startsWith(directoryPath) && name.endsWith(PROPERTIES_EXTENSION)
                        && loadUpgrades == name.contains(UPGRADE_INDICATOR)) {
                    loadTemplateProperties(spriteTemplates, name.substring(name.lastIndexOf(File.separator)+1),
                            zipFile.getInputStream(entry));
                }
            }
        }
    }

    private void loadTemplateProperties(Map<String, Map<String, String>> spriteTemplates, String fileName,
                                        InputStream spritePropertiesStream) throws IOException {
        Properties spriteProperties = new Properties();
        spriteProperties.load(spritePropertiesStream);
        Map<String, String> spritePropertiesMap = new HashMap<>();
        spriteProperties.stringPropertyNames().forEach(propertyName ->
                spritePropertiesMap.put(propertyName, spriteProperties.getProperty(propertyName)));
        String templateName = fileName.replace(PROPERTIES_EXTENSION, "");
        spriteTemplates.put(templateName, spritePropertiesMap);
    }


    private void writeTemplateToFile(Properties templateProperties, File exportFile) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(exportFile);
            templateProperties.store(fileOut, PROPERTIES_COMMENT);
        } catch (IOException e) {
            // TODO - throw custom exception
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    // TODO - throw custom exception
                    e.printStackTrace();
                }
            }
        }
    }

    private String createDirectory(String gameName, String outputPath) {
        String directoryPath = createDirectoryPath(gameName, outputPath);
        createDirectoryIfNonExistent(directoryPath);
        return directoryPath;
    }

    private String createDirectoryPath(String gameName, String outputPath) {
        if (gameName.indexOf(DOT) != -1) {
            return outputPath + gameName.substring(0, gameName.indexOf(DOT)) + File.separator;
        } else {
            return outputPath + gameName + File.separator;
        }
    }

    private void createDirectoryIfNonExistent(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
