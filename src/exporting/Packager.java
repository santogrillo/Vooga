package exporting;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Packages up a game for delivery as an executable JAR. Details what should be included in the JAR must be included
 * in a properties file referenced from the accompanying JarPropertiesGetter class.
 *
 * @author Ben Schwennesen
 */
public final class Packager {

    private final String ERROR_MESSAGE = "The game JAR could not be generated";
    private final String WINDOWS_PATH_DELIMITER_PATTERN = Pattern.quote("\\");
    private final String MANIFEST_FILE_NAME = "META-INF/MANIFEST.MF";
    private final String MANIFEST_VERSION = "1.0";
    private final String CLASS_EXTENSION = ".class";
    private final String JAVA_EXTENSION = ".java";
    private final String JAR_EXTENSION = ".jar";
    private final String LOG_EXTENSION = ".log";
    private final String EMPTY_STRING = "";
    private final char DOT = '.';
    private final int MAX_ENTRY_LENGTH = 2048;

    private JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    private Set<File> compiledFilesToDelete = new HashSet<>();

    private String exportJarOutputPath;
    private Set<String> alreadyAdded = new HashSet<>();

    private JarPropertiesGetter propertiesGetter;
    private JarOutputStream target;

    /**
     * Generate an executable JAR file for an authored game, obtaining the necessary information about parts of the
     * project being included from a properties file.
     *
     * @param gameExportName the name of the game being exported
     * @return the path to the exported JAR file
     */
    public String generateJar(String gameExportName) throws IOException {
        propertiesGetter = new JarPropertiesGetter();
        propertiesGetter.setDisplayedGameName(gameExportName);
        exportJarOutputPath = propertiesGetter.getExportTargetPath(gameExportName);
        try {
            target = initializeJarOutputStream(exportJarOutputPath, propertiesGetter.getMainClassFullName());
            compileAndAddSourceFiles(exportJarOutputPath);
            addResourcesAndData();
            target.close();
        } catch (IOException failedToGenerateJarException) {
            failedToGenerateJarException.printStackTrace();
            throw new IOException(ERROR_MESSAGE);
        }
        return exportJarOutputPath;
    }

    // create a JAR file output stream with the desired main class specified in its manifest
    private JarOutputStream initializeJarOutputStream(String outputPath, String launchClassName) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, MANIFEST_VERSION);
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, launchClassName);
        OutputStream outputStream = new FileOutputStream(outputPath);
        alreadyAdded.add(MANIFEST_FILE_NAME);
        return new JarOutputStream(outputStream, manifest);
    }

    // compile project code and add to the JAR
    private void compileAndAddSourceFiles(String outputPath) throws IOException {
        String sourceDirectoryPath = propertiesGetter.getSourceDirectoryPath();
        String pathToExcludeFromSourceEntries = addTrailingFileSeparatorToPath(sourceDirectoryPath);
        OutputStream compilationLogger = createLogFileOutput(outputPath);
        for (String directoryToIncludePath : propertiesGetter.getSourceDirectoriesToInclude()) {
            File directoryToInclude = new File(directoryToIncludePath);
            compile(directoryToInclude, compilationLogger);
            addToJar(directoryToInclude, pathToExcludeFromSourceEntries, JAVA_EXTENSION);
        }
        compilationLogger.flush();
        compilationLogger.close();
        cleanUp();
    }

    // make a log file to which compilation errors are written
    private OutputStream createLogFileOutput(String outputPath) throws IOException {
        String logPath = outputPath.substring(0, outputPath.lastIndexOf(DOT)) + LOG_EXTENSION;
        File logFile = new File(logPath);
        return new FileOutputStream(logFile);
    }

    // compile a java file
    private void compile(File source, OutputStream compilationLogger) {
        if (source.isDirectory()) {
            compileNestedFiles(source, compilationLogger);
        } else if (source.getName().endsWith(JAVA_EXTENSION)) {
            javaCompiler.run(null, compilationLogger, compilationLogger, source.getAbsolutePath());
            compiledFilesToDelete.add(new File(source.getAbsolutePath().replace(JAVA_EXTENSION, CLASS_EXTENSION)));
        }
    }

    private void compileNestedFiles(File source, OutputStream compilationLogger) {
        File[] sourceFiles = source.listFiles();
        if (sourceFiles != null) {
            for (File subdirectoryOrFile : sourceFiles) {
                compile(subdirectoryOrFile, compilationLogger);
            }
        }
    }

    // delete compiled files after adding them to the JAR
    private void cleanUp() {
        compiledFilesToDelete.removeIf(file -> !file.getPath().endsWith(CLASS_EXTENSION));
        compiledFilesToDelete.forEach(File::delete);
    }

    private void addResourcesAndData() throws IOException {
        for (String resourceRootPath : propertiesGetter.getResourceRoots()) {
            addResourcesRoot(resourceRootPath);
        }
        for (String dataDirectoryPath : propertiesGetter.getDataAndLibraryDirectories()) {
            addToJar(new File(dataDirectoryPath), propertiesGetter.getSourceDirectoryPath(), CLASS_EXTENSION);
        }
    }

    // add the contents of a resources root to the JAR, trimming out the actual root directory
    private void addResourcesRoot(String resourceDirectoryPath) throws IOException {
        File resourceDirectory = new File(resourceDirectoryPath);
        File[] resourceSources = resourceDirectory.listFiles();
        if (resourceSources != null) {
            for (File resourceSource : resourceSources) {
                addToJar(resourceSource, resourceDirectoryPath, EMPTY_STRING);
            }
        }
    }

    private void addToJar(File additionSource, String excludeFromPath, String extensionToIgnore) throws IOException {
        if (!alreadyAdded.contains(additionSource.getPath())) {
            if (additionSource.getPath().endsWith(JAR_EXTENSION)
                    && !additionSource.getPath().equals(exportJarOutputPath)) {
                addExternalLibrary(additionSource);
            } else if (additionSource.isDirectory()) {
                addDirectoryToJar(additionSource, excludeFromPath, extensionToIgnore);
            } else if (extensionToIgnore.isEmpty() || !additionSource.getPath().endsWith(extensionToIgnore)) {
                addFileToJar(additionSource, excludeFromPath);
            }
            alreadyAdded.add(additionSource.getPath());
        }
    }

    // add an external library (another JAR) to the export JAR
    private void addExternalLibrary(File libraryFile) throws IOException {
        JarFile jarFile = new JarFile(libraryFile);
        Set<JarEntry> jarEntries = jarFile.stream().collect(Collectors.toSet());
        for (JarEntry jarEntry : jarEntries) {
            if (!alreadyAdded.contains(jarEntry.getName())) {
                alreadyAdded.add(jarEntry.getName());
                addLibraryJarEntry(jarFile, jarEntry);
            }
        }
    }

    private void addLibraryJarEntry(JarFile jarFile, JarEntry jarEntry) throws IOException {
        target.putNextEntry(jarEntry);
        writeFileToJar(jarFile.getInputStream(jarEntry));
        target.closeEntry();
    }

    // write a directory and its contents to the export JAR
    private void addDirectoryToJar(File source, String excludeFromPath, String extensionToIgnore) throws IOException {
        writeDirectoryJarEntry(source, excludeFromPath);
        File[] nestedFilesAndDirectories = source.listFiles();
        if (nestedFilesAndDirectories != null) {
            for (File nestedElement : nestedFilesAndDirectories) {
                addToJar(nestedElement, excludeFromPath, extensionToIgnore);
            }
        }
    }

    private void writeDirectoryJarEntry(File directory, String excludeFromPath) throws IOException {
        String directoryPathInJarFormat = getJarFormattedPathForFile(directory, excludeFromPath);
        JarEntry entry = new JarEntry(directoryPathInJarFormat);
        entry.setTime(directory.lastModified());
        target.putNextEntry(entry);
        target.closeEntry();
    }

    private void addFileToJar(File file, String excludeFromPath) throws IOException {
        String filePathInJarFormat = getJarFormattedPathForFile(file, excludeFromPath);
        JarEntry entry = new JarEntry(filePathInJarFormat);
        entry.setTime(file.lastModified());
        target.putNextEntry(entry);
        writeFileToJar(new FileInputStream(file));
        target.closeEntry();
    }

    private void writeFileToJar(InputStream sourceStream) throws IOException {
        InputStream in = new BufferedInputStream(sourceStream);
        byte[] buffer = new byte[MAX_ENTRY_LENGTH];
        int count;
        while ((count = in.read(buffer)) != -1) {
            target.write(buffer, 0, count);
        }
        in.close();
    }

    // obtain the path of a file being added to a JAR in the format JAR requires
    private String getJarFormattedPathForFile(File file, String partOfPathToExclude) {
        String convertedPath = file.getPath().replaceAll(WINDOWS_PATH_DELIMITER_PATTERN, File.separator);
        if (file.isDirectory()) {
            convertedPath = addTrailingFileSeparatorToPath(convertedPath);
        }
        return convertedPath.replaceFirst(partOfPathToExclude, EMPTY_STRING);
    }

    // ensure that directories' paths are written correctly since they need a trailing file separator (/)
    private String addTrailingFileSeparatorToPath(String path) {
        return path.endsWith(File.separator) ? path : path + File.separator;
    }
}
