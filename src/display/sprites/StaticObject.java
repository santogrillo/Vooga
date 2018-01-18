package display.sprites;

import javafx.scene.image.Image;
import display.splashScreen.ScreenDisplay;

/**
 * 
 * @author bwelton, moboyle
 *
 */
public class StaticObject extends InteractiveObject{
	private int objectSize;
	private int realSize;
	
	public StaticObject(int cellSize, ScreenDisplay display, String name) {
		super(display, name);
		setSize(cellSize);
		Image image;
		try {
			image = new Image(getClass().getClassLoader().getResourceAsStream(name));
		}catch(NullPointerException e) {
			image = new Image(name);
		}
		this.setImage(image);
		objectSize = cellSize;
		
	}

	private void setSize(int size) {
		realSize = size * sizeOfCell();
		this.setFitWidth(realSize);
		this.setFitHeight(realSize);
	}
	
	public double getHeight() {
		return this.getFitHeight();
	}
	
	public double getWidth() {
		return this.getFitWidth();
	}
	
	@Override
	public int getSize() {
		return realSize;
	}
	
	@Override
	public int getCellSize() {
		return objectSize;
	}

	public void incrementSize() {
		objectSize++;
		setSize(objectSize);
	}
	
	public void decrementSize() {
		if (objectSize > 1) {
			objectSize--;
			setSize(objectSize);
		}
	}
}
