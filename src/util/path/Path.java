package util.path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import authoring.GameArea;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * 
 * @author bwelton
 *
 */
public class Path extends Group{
	private Set<PathPoint> points;
	private List<PathPoint> headPoints;
	private PathPoint activePoint;
	private Color pathColor;
	private GameArea definedArea;
	
	public Path(GameArea area) {
		Random rand = new Random();
		pathColor = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		points = new HashSet<>();
		headPoints = new ArrayList<>();
		activePoint = null;
		definedArea = area;
	}
	
	public boolean addWaypoint(MouseEvent e, double x, double y) {
		e.consume();
		if(activePoint == null && points.size() != 0) return false;
		PathPoint point = new PathPoint(x, y, pathColor);
		point.addEventHandler(MouseEvent.MOUSE_PRESSED, event->handlePointClick(event, point));
		
		if(activePoint != null) {
			drawLineBetween(activePoint, point);
		}else {
			headPoints.add(point);
		}
		
		setActiveWaypoint(point);
		activePoint = point;
		points.add(point);
		this.getChildren().add(point);
		return true;
	}
	
	private void handlePointClick(MouseEvent e, PathPoint point) {
		e.consume();
		if(point.wasMoved()) {
			point.lockPosition();
		}else if(e.getButton() == MouseButton.PRIMARY && e.isControlDown()) {
			connectPath(e, point);
		}else if(e.getButton() == MouseButton.PRIMARY) {
			setActiveWaypoint(point);
		}else if(e.getButton() == MouseButton.SECONDARY) {
			removeWaypoint(e, point);
		}
	}
	
	private void removeWaypoint(MouseEvent e, PathPoint point) {
		points.remove(point);
		removeWaypointLines(point);
		modifyLineOrder(point);
		if(point.equals(activePoint)) activePoint = null;
		if(headPoints.contains(point)) headPoints.remove(point);
		this.getChildren().remove(point);
	}
	
	private void setActiveWaypoint(PathPoint point) {
		point.toggleActive();
		if(point.equals(activePoint)) {
			activePoint = null;
		}else {
			if(activePoint != null) activePoint.toggleActive();
			activePoint = point;
			definedArea.updateActivePath(this);
		}
	}
	
	private void connectPath(MouseEvent e, PathPoint point) {
		if(!point.equals(activePoint) && activePoint != null) {
			drawLineBetween(activePoint, point);
			setActiveWaypoint(point);
		}
	}
	
	private void removeWaypointLines(PathPoint point) {
		for(PathPoint prev:point.getPrevLines().keySet()) {
			PathLine line = prev.getNextLines().remove(point);
			this.getChildren().remove(line.getNode());
		}
		
		for(PathPoint next:point.getNextLines().keySet()) {
			PathLine line = next.getPrevLines().remove(point);
			this.getChildren().remove(line.getNode());
		}

	}
	
	private void modifyLineOrder(PathPoint point) {
		for(PathPoint prevPoint:point.getPrevLines().keySet()) {
			for(PathPoint nextPoint:point.getNextLines().keySet()) {
				drawLineBetween(prevPoint, nextPoint);
			}
		}
	}
	
	private void handleLineClick(MouseEvent e, PathLine line) {
		e.consume();
		if(e.getButton() == MouseButton.PRIMARY && e.isControlDown()) {
			line.changeDirection();
		}else if(e.getButton() == MouseButton.PRIMARY) {
			setActiveLine(line);
		}else if(e.getButton() == MouseButton.SECONDARY) {
			removeLine(line);
		}
	}

	private void setActiveLine(PathLine line) {
		line.toggleActive();
	}
	
	private void removeLine(PathLine line) {
		if(!line.isActive()) return;
		line.removeLineFromPoints();
		this.getChildren().remove(line.getNode());
	}

	private void drawLineBetween(PathPoint start, PathPoint end) {
		PathLine line = start.setConnectingLine(end);
		this.getChildren().add(line.getNode());
		line.getNode().toBack();
		line.addEventHandler(MouseEvent.MOUSE_PRESSED, e->handleLineClick(e, line));
	}
	
	public List<PathPoint> getStartPoints(){
		return new ArrayList<>(headPoints);
	}
	
	public Color getColor() {
		return pathColor;
	}
	
	public void deactivate() {
		if(activePoint != null) setActiveWaypoint(activePoint);
	}
	
}
