package engine.behavior.collision;

import engine.game_elements.ElementProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Exclude;

/**
 * Encapsulates game elements' collision fields and behavior. Responsible for checking for and handling collisions.
 *
 * @author Ben Schwennesen
 */
public class CollisionHandler {

    private CollisionVisitor collisionVisitor;
    private CollisionVisitable collisionVisitable;

    private String imageUrl;
    private double imageHeight;
    private double imageWidth;
    private final String DEFAULT_EXPLOSION = "";
    @Exclude private ImageView graphicalRepresentation;

    public CollisionHandler(CollisionVisitor collisionVisitor, CollisionVisitable collisionVisitable,
                            @ElementProperty(value = "imageUrl", isTemplateProperty = true) String imageUrl,
                            @ElementProperty(value = "imageHeight", isTemplateProperty = true) double imageHeight,
                            @ElementProperty(value = "imageWidth", isTemplateProperty = true) double imageWidth) {
        this.collisionVisitor = collisionVisitor;
        this.collisionVisitable = collisionVisitable;
        this.imageUrl = imageUrl;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        constructGraphicalRepresentation();
    }

    private void constructGraphicalRepresentation() {
        Image image;
        try {
            image = new Image(imageUrl);
            graphicalRepresentation = new ImageView(image);
            graphicalRepresentation.setFitHeight(imageHeight);
            graphicalRepresentation.setFitWidth(imageWidth);
        } catch (NullPointerException | IllegalArgumentException imageUrlNotValidException) {
            graphicalRepresentation = new ImageView();
            graphicalRepresentation.setVisible(false);
        }
    }


    public boolean collidesWith(CollisionHandler other) {
        return (other.graphicalRepresentation.getBoundsInLocal()
                .intersects(this.graphicalRepresentation.getBoundsInLocal())) && 
        		(other.getPlayerId()!=this.getPlayerId() && other.getPlayerId()!=0 && this.getPlayerId()!=0);
    }

    public void processCollision(CollisionHandler other) {
    	other.collisionVisitable.accept(collisionVisitor);
    }

    public boolean isBlocked() {
        return collisionVisitor.isBlocked();
    }

    public void unBlock() {
        collisionVisitor.unBlock();
    }

    public boolean isAlive() {
        return collisionVisitor.isAlive();
    }

    /**
     * Get the unique identifier corresponding to player owning this sprite.
     *
     * @return identifier of player controlling this sprite
     */
    public int getPlayerId() {
        return collisionVisitor.getPlayerId();
    }

    public void setGraphicalRepresentation(ImageView graphicalRepresentation) {
        this.graphicalRepresentation = graphicalRepresentation;
    }
    
    public ImageView getGraphicalRepresentation() {
    	if (graphicalRepresentation == null) {
        	constructGraphicalRepresentation();    		
    	}
        return graphicalRepresentation;
    }
    
    public String getImageUrl() {
    		return imageUrl;
    }
    
    public String getAudioUrl() {
    	return collisionVisitable.getAudioUrl();
    }
    
    public double getBlastRadius() {
    	return collisionVisitable.getBlastRadius();
    }
    
    public boolean shouldExplode() {
    	return !collisionVisitor.explode().equals(DEFAULT_EXPLOSION);
    }
    
    public String explode() {
    	return collisionVisitor.explode();
    }
}
