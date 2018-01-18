package engine.behavior.movement;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import util.Exclude;

/**
 * Responsible for tracking the current position of a game element.
 *
 * @author Ben Schwennesen
 */
public class LocationProperty {

    @Exclude private DoubleProperty xCoordinate;
    @Exclude private DoubleProperty yCoordinate;
    private double xCoordinateSerializable;
    private double yCoordinateSerializable;

    /**
     * Constructs a point object for tracking the location of a game element.
     *
     * @param xCoordinate the double property responsible for tracking the element's horizontal position
     * @param yCoordinate the double property responsible for tracking the element's vertical position
     */
    public LocationProperty(DoubleProperty xCoordinate, DoubleProperty yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        xCoordinateSerializable = xCoordinate.doubleValue();
        yCoordinateSerializable = yCoordinate.doubleValue();
    }

    /**
     * Get the current x-coordinate of the point.
     *
     * @return the current horizontal coordinate
     */
    public double getCurrentX() {
        reinitializeXPropertyIfNecessaryAndSet();
        return xCoordinate.get();
    }

    /**
     * Get the current y-coordinate of the point.
     *
     * @return the current vertical coordinate
     */
    public double getCurrentY() {
        reinitializeYPropertyIfNecessaryAndSet();
        return yCoordinate.get();
    }

    /**
     * Set the current x-coordinate of the point.
     */
    public void setX(double newXCoordinate) {
        xCoordinateSerializable = newXCoordinate;
        reinitializeXPropertyIfNecessaryAndSet();

    }

    /**
     * Set the current y-coordinate of the point.
     */
    public void setY(double newYCoordinate) {
        yCoordinateSerializable = newYCoordinate;
        reinitializeYPropertyIfNecessaryAndSet();
    }

    private void reinitializeXPropertyIfNecessaryAndSet() {
        if (xCoordinate == null) {
            xCoordinate = new SimpleDoubleProperty(xCoordinateSerializable);
        } else {
            xCoordinate.set(xCoordinateSerializable);
        }
    }

    private void reinitializeYPropertyIfNecessaryAndSet() {
        if (yCoordinate == null) {
            yCoordinate = new SimpleDoubleProperty(yCoordinateSerializable);
        } else {
            yCoordinate.set(yCoordinateSerializable);
        }
    }
}
