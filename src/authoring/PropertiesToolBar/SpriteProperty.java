package authoring.PropertiesToolBar;

public class SpriteProperty {
	private String myName;
	private String myValue;

	public SpriteProperty(String rowName, String value) {
		myName = rowName;
		myValue = value;
	}
	
	
	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	public String getMyValue() {
		return myValue;
	}

	public void setMyValue(String myValue) {
		this.myValue = myValue;
	}
	
}