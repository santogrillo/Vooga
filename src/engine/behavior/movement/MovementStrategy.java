package engine.behavior.movement;

import javafx.geometry.Point2D;

/**
 * Used by game elements to handle movement according to various strategies which are selected during game authoring.
 *
 * @author Ben Schwennesen
 */
public interface MovementStrategy {

    /**
     * Move based on the specific movement strategy for the game element
     *
     * @return the new point-based location of the element
     */
    Point2D move();

    /**
     * Retrieve the current horizontal coordinate for the game element
     *
     * @return current x-coordinate
     */
    double getCurrentX();

    /**
     * Retrieve the current vertical coordinate for the game element
     *
     * @return current y-coordinate
     */
    double getCurrentY();

    /**
     * Set horizontal coordinate for the game element using this strategy object.
     *
     * @param newXCoord new x-coordinate to use
     */
    void setX(double newXCoord);

    /**
     * Set vertical coordinate for the game element using this strategy object.
     *
     * @param newYCoord new y-coordinate to use
     */
    void setY(double newYCoord);

    /**
     * Get auto-updating position of the game element using this strategy. This can be used to track the element as it
     * moves in the game.
     *
     * @return point object that changes with movement
     */
    LocationProperty getPositionForTracking();
    
    /**
     * Get if the Sprite has reached its destination
     * 
     * @return Whether or not the target has reached its destination
     * */
    boolean targetReached();
    
    /**
     * Get the facing angle of the element
     * 
     * @return facing angle of element
     */
    double getAngle();
    
    /**
     * Get if the Sprite should be removed upon completion of the its movement
     * 
     * @return Whether or not the Sprite should be removed
     * */
    boolean removeUponCompletion();
}
