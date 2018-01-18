package authoring.PropertiesToolBar;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

public class NewTest extends HBox {
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	
	public NewTest() {
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, e->mouseClicked(e));
		this.addEventHandler(MouseEvent.MOUSE_RELEASED, e->mouseReleased(e));
	}
	
	private void mouseClicked(MouseEvent e) {
		startX = e.getX();
		startY = e.getY();
	}
	
	private void mouseReleased(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
		this.getChildren().add(new Line(startX, startY, endX, endY));
	}
//	public void addDrawer() {
//		myScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->drawLine(e));
//	}
//	
//	private void drawLine(MouseEvent e) {
//		rootAdd(new Line(e.getX(), e.getY(), e.getX()+1,e.getY()+1));
//	}

}
