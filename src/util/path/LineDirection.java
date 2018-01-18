package util.path;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * 
 * @author bwelton
 *
 */
public class LineDirection extends Polygon implements Serializable{
	private PathLine line;
	
	public LineDirection(PathPoint start, PathPoint end, PathLine line, Color color) {
		this.line = line;
		drawShape();
		this.setFill(color);
		line.startXProperty().addListener((observable, oldValue, newValue) -> drawShape());
		line.startYProperty().addListener((observable, oldValue, newValue) -> drawShape());
		line.endXProperty().addListener((observable, oldValue, newValue) -> drawShape());
		line.endYProperty().addListener((observable, oldValue, newValue) -> drawShape());
	}
	
	protected void drawShape() {
		this.getPoints().clear();
		PathPoint start = line.getStartPoint();
		PathPoint end = line.getEndPoint();
		
		double angle = Math.atan2(end.getCenterY()-start.getCenterY(), end.getCenterX() - start.getCenterX());
		double midx = (start.getCenterX() + end.getCenterX())/2;
		double midy = (start.getCenterY() + end.getCenterY())/2;
		
		double tipx = midx + 5*Math.cos(angle);
		double tipy = midy + 5*Math.sin(angle);
		double vert1x = midx - 5*Math.cos(angle) + 5*Math.cos(angle + (Math.PI/2));
		double vert1y = midy - 5*Math.sin(angle) + 5*Math.sin(angle + (Math.PI/2));
		double vert2x = midx - 5*Math.cos(angle) - 5*Math.cos(angle + (Math.PI/2));
		double vert2y = midy - 5*Math.sin(angle) - 5*Math.sin(angle + (Math.PI/2));
		
		this.getPoints().addAll(new Double[] {
				tipx, tipy, vert1x, vert1y, vert2x, vert2y
		});
	}
}
