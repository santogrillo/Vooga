package util;

import java.io.File;

/**
 * Remove temporary files created for live editing.
 *
 * @author Ben Schwennesen
 */
public class Purger {

    private final String TEMPORARY_LIVE_EDITING_FILE = "authoring/temp.voog";
    private final String[] TEMPORARY_FILE_DIRECTORIES = { "authoring/waves/temp/",
            "authoring/sprite-templates/temp/" };

    /**
     * Remove all the temporary files associated with a game temporarily launched from the authoring environment.
     */
    public void purge() {
        try {
            File tempSaveFile = new File(TEMPORARY_LIVE_EDITING_FILE);
            tempSaveFile.delete();
            for (String tempDirectory : TEMPORARY_FILE_DIRECTORIES) {
                File tempSaveDirectory = new File(tempDirectory);
                purgeDirectory(tempSaveDirectory);
            }
        } catch (SecurityException failedToDeleteException) {
            // ignore since file will not cause the problems motivating purge() in this case
        }
    }

    private void purgeDirectory(File tempSaveDirectory) {
        File[] filesInDirectory = tempSaveDirectory.listFiles();
        if (filesInDirectory != null) {
            for (File nestedFile : filesInDirectory) {
                if (nestedFile.isDirectory()) {
                    purgeDirectory(nestedFile);
                } else {
                    nestedFile.delete();
                }
            }
        }
        tempSaveDirectory.delete();
    }
}
