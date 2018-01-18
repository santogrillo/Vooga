package authoring;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author moboyle
 *
 */
public class ObjectProperties {
	 
    private final SimpleStringProperty firstCol;
    private final SimpleStringProperty lastCol;

    public ObjectProperties(String first, String last) {
        this.firstCol = new SimpleStringProperty(first);
        this.lastCol = new SimpleStringProperty(last);
    }

    public String getFirst() {
        return firstCol.get();
    }

    public void setFirst(String data) {
        firstCol.set(data);
    }

    public String getLast() {
        return lastCol.get();
    }

    public void setLastName(String data) {
        lastCol.set(data);
    }

}
