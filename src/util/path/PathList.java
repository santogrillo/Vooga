package util.path;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Point2D;

/**
 * 
 * @author bwelton
 *
 */
public class PathList extends LinkedList {

	private static final long serialVersionUID = -7588980448693010399L;

	private PathNode current;
	private PathNode head;
	private PathPoint headpoint;
	private PathNode constructor;
	private Map<PathPoint, PathNode> pointsToNodes;
	private LinkedList<PathPoint> orderedPoints;

	public PathList(PathPoint start) {
		pointsToNodes = new HashMap<>();
		orderedPoints = new LinkedList<>();
		head = new PathNode(start);
		headpoint = start;
		current = head;
		constructor = head;
		
		orderedPoints.add(start);
	}
	
	public Point2D next() {
		if(current == null) return null;
		double x = current.x;
		double y = current.y;
		current = current.next;
		return new Point2D(x, y);
	}
	
	
	protected boolean add(PathPoint point) {
		orderedPoints.add(point);
		if(pointsToNodes.containsKey(point)) {
			constructor.next = pointsToNodes.get(point);
			constructor = constructor.next;
			return false;
		}else {
			PathNode node = new PathNode(point);
			constructor.next = node;
			constructor = constructor.next;
			pointsToNodes.put(point, node);
			return true;
		}
	}
	
	public PathList clone() {
		PathList copy = new PathList(headpoint);
		for(PathPoint point:orderedPoints) {
			copy.add(point);
		}
		return copy;
	}

	public String writeToSerializationFile() throws IOException {
		final String PATH_FILE_DIRECTORY = "authoring/serializations/";
		final String PATH = "path";
		final String SERIALIZED_EXTENSION = ".ser";
		File directory = new File(PATH_FILE_DIRECTORY);
		if(!directory.exists()) {
			directory.mkdir();
		}
		String filePath = PATH_FILE_DIRECTORY + PATH + orderedPoints.hashCode() + SERIALIZED_EXTENSION;
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(file);
		ObjectOutputStream objectOutput = new ObjectOutputStream(out);
		objectOutput.writeObject(this);
		out.flush();
		return filePath;
	}
	
	private class PathNode implements Serializable {
		private double x;
		private double y;
		private PathNode next = null;

		private static final long serializationUID = 1113799434508676095L;
		
		private PathNode(PathPoint next) {
			this.x = next.getCenterX();
			this.y = next.getCenterY();
			this.next = null;
		}
	}


}
