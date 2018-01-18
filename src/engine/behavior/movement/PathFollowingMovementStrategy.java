package engine.behavior.movement;

import javafx.scene.paint.Color;
import util.path.PathList;
import engine.game_elements.ElementProperty;
import javafx.geometry.Point2D;
import util.path.PathPoint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Movement strategy for objects that move along a defined path
 * 
 * @author mscruggs
 *
 */
public class PathFollowingMovementStrategy extends TargetedMovementStrategy {

	private PathList coordinates;
	private Point2D target;

	public PathFollowingMovementStrategy(
			@ElementProperty(value = "velocity", isTemplateProperty = true) double velocity,
			@ElementProperty(value = "pathListFile", isTemplateProperty = true) String pathListFile) {
		super(new Point2D(0,0),0, 0, velocity);
		coordinates = deserializePath(pathListFile);
		Point2D startCoordinates = coordinates.next();
		setX(startCoordinates.getX());
		setY(startCoordinates.getY());
		setPathCoordinates(coordinates);
	}

	private PathList deserializePath(String pathListFile) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(pathListFile);
		try {
			ObjectInputStream deserializer = new ObjectInputStream(in);
			return (PathList) deserializer.readObject();
		} catch (IOException | ClassNotFoundException | NullPointerException failedToLoadStreamException) {
			try {
				ObjectInputStream deserializer = new ObjectInputStream(new FileInputStream(pathListFile));
				return (PathList) deserializer.readObject();
			} catch (IOException | ClassNotFoundException failedToLoadFileException) {
				return new PathList(new PathPoint(0,0, Color.BLACK));
			}
		}
	}

	public Point2D move() {
		super.move();
		checkIfLocationReached();
		return getCurrentCoordinates();
	}

	public void setPathCoordinates(PathList coordinates) {
		this.coordinates = coordinates;
		this.target = this.coordinates.next();
		setTargetCoordinates(target.getX(), target.getY());
	}

	public boolean targetReached() {
		return (target == null);
	}

	/**
	 * Check to see if one point in the coordinates was reached
	 */
	private void checkIfLocationReached() {
		if (super.targetReached()) {
			target = coordinates.next();
			System.out.println("===================================");
			System.out.println(this);
			System.out.println(target);
			System.out.println("===================================");
			
			if (target == null) {
				stop();
			} else {
				this.setTargetCoordinates(target.getX(), target.getY());
			}
		}
	}
}
