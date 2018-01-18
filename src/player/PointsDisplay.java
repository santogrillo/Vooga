package player;

import util.PropertiesGetter;

/**
 * 
 * @author mmosca
 *
 */
public class PointsDisplay extends ValueDisplay {
	private final String LABEL = "points";
	private final double XPOS = 850;
	private final double YPOS = 0;
	
	public PointsDisplay() {
		super();
		setStandardDisplayLabel(PropertiesGetter.getProperty(LABEL));
		addItemsWithLabel();
	}
}
