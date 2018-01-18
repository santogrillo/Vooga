package display.interfaces;

import java.util.List;
import java.util.Map;

import util.path.Path;
import javafx.scene.paint.Color;
import display.sprites.InteractiveObject;

public interface Droppable {

	public void droppedInto(InteractiveObject interactive);
	
	public void objectRemoved(InteractiveObject interactive);
	
	public void freeFromDroppable(InteractiveObject interactive);
	
	public Map<Path, Color> getPaths();
}
