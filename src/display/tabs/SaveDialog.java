package display.tabs;

import java.io.File;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * 
 * @author bwelton, sgrillo
 *
 */
public class SaveDialog {
	public static File SaveLocation(Scene s)
	{
		FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Save Level");
		 fileChooser.getExtensionFilters().add(
		         new ExtensionFilter("Vooga Game", "*.voog"));
		 File selectedFile = fileChooser.showSaveDialog(s.getWindow());
		 return selectedFile;
	}
}
