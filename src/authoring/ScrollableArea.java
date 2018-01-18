package authoring;

import com.sun.prism.paint.Color;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

/**
 * 
 * @author bwelton
 *
 */
public class ScrollableArea extends ScrollPane{
	private final int X_OFFSET = 260;
	private final int Y_OFFSET = 50;
	private final int SIZE = 400;
	
	private GameArea area;
	
	public ScrollableArea(GameArea area) {
		this.area = area;
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setLayoutX(X_OFFSET);
		this.setLayoutY(Y_OFFSET);
		this.setPrefSize(SIZE, SIZE);
		this.setStyle("-fx-background: black;");
		this.setContent(area);
		this.addEventHandler(ZoomEvent.ZOOM, e->zoom(e));
		this.addEventFilter(ScrollEvent.ANY, e->scroll(e));
	}
	
	private void zoom(ZoomEvent e) {
		double scaleX = e.getZoomFactor()*area.getScaleX();
		double scaleY = e.getZoomFactor()*area.getScaleY();
		
		if(scaleX <= 1 && scaleY <= 1 && (scaleX*area.getHeight() >= SIZE) && (scaleY*area.getWidth() >= SIZE)) {
			area.setScaleX(area.getScaleX()* e.getZoomFactor());
			area.setScaleY(area.getScaleY()* e.getZoomFactor());
			this.setHvalue(0.5);
			this.setVvalue(0.5);
		}
	}
	
	private void scroll(ScrollEvent e) {
		if(outOfBounds(e)) {
			e.consume();
		}
	}
	
	private boolean outOfBounds(ScrollEvent e) {
		//left bound
		if((0.5 - this.getHvalue()) >= Math.abs(0.5 - area.getScaleX()) && e.getDeltaX()>0) {
			return true;
		//right bound
		}else if((this.getHvalue()-0.5) >= Math.abs(0.5 - area.getScaleX()) && e.getDeltaX()<0) {
			return true;
		//top bound
		}else if((0.5 - this.getVvalue()) >= Math.abs(0.5 - area.getScaleY()) && e.getDeltaY()>0) {
			return true;
		//bottom bound
		}else if((this.getVvalue()-0.5) >= Math.abs(0.5 - area.getScaleY()) && e.getDeltaY()<0) {
			return true;
		}
		return false;
	}
	
	public void changeLevel(GameArea newArea) {
		this.area = newArea;
		this.setContent(newArea);
		this.setHvalue(0);
		this.setVvalue(0);
	}
}
