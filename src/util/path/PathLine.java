package util.path;

import java.io.Serializable;
import java.util.ResourceBundle;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * 
 * @author bwelton
 *
 */
public class PathLine extends Line implements Serializable{
	private final static String WIDTH = "Path_Width";
	private final static String INACTIVE = "Path_Color";
	private final static String ACTIVE = "Path_Active_Color";
	
	private transient ResourceBundle pathProperties;
	private PathPoint start;
	private PathPoint end;
	private LineDirection direction;
	private transient Group node;
	private boolean active = false;
	private transient Color activeColor;
	private transient Color inactiveColor;
	private int width;
	
	public PathLine(PathPoint start, PathPoint end, Color color) {
		initializeProperties();
		
		this.start = start;
		this.end = end;
		this.inactiveColor = color;
		this.startXProperty().bind(start.centerXProperty());
		this.startYProperty().bind(start.centerYProperty());
		this.endXProperty().bind(end.centerXProperty());
		this.endYProperty().bind(end.centerYProperty());
		this.setStroke(inactiveColor);
		this.setStrokeWidth(width);
		
		direction = new LineDirection(start, end, this, inactiveColor);
		node = new Group();
		node.getChildren().add(direction);
		node.getChildren().add(this);
	}
	
	private void initializeProperties() {
		pathProperties = ResourceBundle.getBundle("authoring/resources/Path");
		width = Integer.parseInt(pathProperties.getString(WIDTH));
		activeColor = Color.web(pathProperties.getString(ACTIVE));
//		inactiveColor = Color.web(pathProperties.getString(INACTIVE));
	}

	protected void toggleActive() {
		if(!active) {
			this.setStroke(activeColor);
		}else {
			this.setStroke(inactiveColor);
		}
		active = !active;
	}
	
	protected void removeLineFromPoints() {
		start.getNextLines().remove(end);
		end.getPrevLines().remove(start);
	}
	
	protected void changeDirection() {
		removeLineFromPoints();
		
		PathPoint temp = start;
		start = end;
		end = temp;
		
		direction.drawShape();
		start.getNextLines().put(end, this);
		end.getPrevLines().put(start, this);
	}
	
	protected boolean isActive() {
		return active;
	}
	
	protected Group getNode() {
		return node;
	}
	
	protected PathPoint getStartPoint() {
		return start;
	}
	
	protected PathPoint getEndPoint() {
		return end;
	}
}
