package player;

import util.PropertiesGetter;

/**
 * 
 * @author bwelton
 *
 */
public class CoinDisplay extends ValueDisplay {
	private final String IMAGE = "coinImage";
	private final double XPOS = 700;
	private final double YPOS = 0;
	
	public CoinDisplay() {
		super();
		setStandardDisplayImage(PropertiesGetter.getProperty(IMAGE));
		setStandardImageViewSize();
		setBoxPosition(XPOS, YPOS);
		addItemsWithImage();
	}
}
