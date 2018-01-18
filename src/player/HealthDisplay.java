package player;

import util.PropertiesGetter;

public class HealthDisplay extends ValueDisplay {
	private final String IMAGE = "heartImage";
	private final double XPOS = 600;
	private final double YPOS = 0;
	
	public HealthDisplay() {
		super();
		setStandardDisplayImage(PropertiesGetter.getProperty(IMAGE));
		setStandardImageViewSize();
		addItemsWithImage();
	}
}
