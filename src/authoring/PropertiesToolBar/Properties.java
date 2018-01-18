package authoring.PropertiesToolBar;

import javafx.geometry.Point2D;

/**
 * 
 * @author moboyle, bwelton
 *
 */
public class Properties {
	private String myProperty;
	private Object myObject;
	private String myValue;
	private Class myClass;

	public Properties(String rowName, Object object) {
		myProperty = rowName;
		myObject = object;
		myClass = object.getClass();
		myValue = object.toString();
	}
	
	
	public String getMyProperty() {
		return myProperty;
	}

	public void setMyProperty(String myName) {
		this.myProperty = myName;
	}

	public String getMyValue() {
		return myValue;
	}
	
	public Object getMyObject() {
		return myObject;
	}
	
	public void setMyObject(Object object) {
		if(myClass.getName() != object.getClass().getName()) {
			throw new IllegalArgumentException();
		}else {
			this.myObject = object;
		}
	}

	public void setMyValue(String value) {
		Object newObject = parseValue(value);
		setMyObject(newObject);
		this.myValue = value;
	}
	
	private Object parseValue(String value) {
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException nonInteger) {
			try {
				return Double.parseDouble(value);
			}catch(NumberFormatException | NullPointerException nonDouble) {
				return value;
			}
		}
	}
	
}