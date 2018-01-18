package authoring;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import display.sprites.BackgroundObject;
import display.sprites.InteractiveObject;

/**
 * 
 * @author bwelton, moboyle
 *
 */
public class Cell extends StackPane{
	private boolean active = false;
	private List<InteractiveObject> myAssignments;
	private List<InteractiveObject> myBackgrounds;
	
	public Cell() {
		myAssignments = new ArrayList<>();
		myBackgrounds = new ArrayList<>();
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, e->highlight());
		this.addEventHandler(MouseEvent.MOUSE_EXITED, e->removeHighlight());
	}

	private void highlight() {
		this.setStyle("-fx-border-color:black;");
	}

	private void removeHighlight() {
		this.setStyle("-fx-background-color:transparent;");
	}
	
	protected boolean pathActive() {
		return active;
	}
	
	protected void activate() {
		active = true;
	}
	
	protected void deactivate() {
		active = false;
	}
	
	protected void assignToCell(InteractiveObject currObject) {
		if (currObject instanceof BackgroundObject) {
			myBackgrounds.add((BackgroundObject) currObject);
		} else {
			myAssignments.add(currObject);
		}
	}
	
	protected boolean isEmpty() {
		return myAssignments.isEmpty();
	}

	public void removeAssignment(InteractiveObject interactive) {
		if (!isEmpty()) myAssignments.remove(interactive);
	}
	
	public List<InteractiveObject> saveAssignments() {
		return myAssignments;
	}
}
