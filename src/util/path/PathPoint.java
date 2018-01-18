package util.path;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 
 * @author bwelton
 *
 */
public class PathPoint extends Circle implements Serializable {
	private final static String RADIUS = "Path_Point_Radius";
	private final static String INACTIVE = "Path_Point_Color";
	private final static String ACTIVE = "Path_Active_Color";
	
	private Map<PathPoint,PathLine> linesToPrev;
	private Map<PathPoint,PathLine> linesToNext;
	private transient ResourceBundle pathProperties;
	private boolean active = false;
	private boolean wasDragged = false;
	private transient Color activeColor;
	private transient Color inactiveColor;
	private int radius;
	
	public PathPoint(double x, double y, Color color) {
		linesToPrev = new HashMap<>();
		linesToNext = new HashMap<>();
		initializeProperties();
		initializeHandlers();
		
		this.setCenterX(x);
		this.setCenterY(y);
		this.setRadius(radius);
		this.setFill(activeColor);
		inactiveColor = color;
	}
	
	private void initializeHandlers() {
		this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->dragPoint(e));
	}
	
	private void initializeProperties() {
		pathProperties = ResourceBundle.getBundle("authoring/resources/Path");
		radius = Integer.parseInt(pathProperties.getString(RADIUS));
		activeColor = Color.web(pathProperties.getString(ACTIVE));
//		inactiveColor = Color.web(pathProperties.getString(INACTIVE));
	}

	protected PathLine setConnectingLine(PathPoint next) {
		PathLine line = new PathLine(this, next, inactiveColor);
		linesToNext.put(next, line);
		next.addToPrevious(this, line);
		return line;
	}
	
	protected void addToPrevious(PathPoint prev, PathLine line) {
		linesToPrev.put(prev, line);
	}
	
	protected void lockPosition() {
		wasDragged = false;
	}
	
	protected boolean wasMoved() {
		return wasDragged;
	}
	
	private void dragPoint(MouseEvent e) {
		this.setCenterX(e.getX());
		this.setCenterY(e.getY());
		wasDragged = true;
		e.consume();
	}
	
	protected void toggleActive() {
		if(!active) {
			this.setFill(activeColor);
		}else {
			this.setFill(inactiveColor);
		}
		active = !active;
	}
	
	protected boolean isActive() {
		return active;
	}
	
	protected Map<PathPoint, PathLine> getPrevLines(){
		return linesToPrev;
	}
	
	protected Map<PathPoint, PathLine> getNextLines(){
		return linesToNext;
	}

	/*private void writeObject(ObjectOutputStream out) throws IOException {

	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {

	}*/

}
