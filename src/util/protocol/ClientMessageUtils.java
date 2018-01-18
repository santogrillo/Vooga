package util.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.Update;

public class ClientMessageUtils {

	private Map<Integer, ImageView> idsToImageViews = new HashMap<>();
	private Collection<ImageView> newImageViews = new HashSet<>();
	private Collection<ImageView> deletedImageViews = new HashSet<>();

	public void initializeLoadedLevel(LevelInitialized levelInitialized) {
		if (levelInitialized.hasError()) {
			// TODO - Handle error - display dialog to user
			return;
		}
		if (levelInitialized.hasSpritesAndStatus()) {
			handleSpriteUpdates(levelInitialized.getSpritesAndStatus());
		}
		if (levelInitialized.hasInventory()) {
			// Could choose to initialize here, but it appears the front end does it in
			// Toolbar.initializeInventory() with hardcoded sizes?
		}
	}

	public Collection<ImageView> getNewImageViews() {
		return newImageViews;
	}

	public Collection<ImageView> getDeletedImageViews() {
		return deletedImageViews;
	}

	public Collection<Integer> getCurrentSpriteIds() {
		return idsToImageViews.keySet();
	}

	public void clearChanges() {
		newImageViews.clear();
		deletedImageViews.clear();
	}

	public void handleSpriteUpdates(Update update) {
		registerNewlyGeneratedSprites(update);
		update.getSpriteUpdatesList().forEach(updatedSprite -> updateSpriteDisplay(updatedSprite));
		registerNewlyDeletedSprites(update);
	}

	public ImageView getRepresentationFromSpriteId(int id) {
		return idsToImageViews.get(id);
	}

	public int addNewSpriteToDisplay(NewSprite newSprite) {
		ImageView imageViewForSprite = new ImageView(new Image(newSprite.getImageURL()));
		imageViewForSprite.setFitHeight(newSprite.getImageHeight());
		imageViewForSprite.setFitWidth(newSprite.getImageWidth());
		imageViewForSprite.setX(newSprite.getSpawnX());
		imageViewForSprite.setY(newSprite.getSpawnY());
		int spriteId = newSprite.getSpriteId();
		imageViewForSprite.setId(Integer.toString(spriteId));
		idsToImageViews.put(spriteId, imageViewForSprite);
		return spriteId;
	}

	public ImageView removeDeadSpriteFromDisplay(SpriteDeletion spriteDeletion) {
		return idsToImageViews.remove(spriteDeletion.getSpriteId());
	}

	public void updateSpriteDisplay(SpriteUpdate updatedSprite) {
		ImageView imageViewForSprite = idsToImageViews.get(updatedSprite.getSpriteId());
		imageViewForSprite.setX(updatedSprite.getNewX());
		imageViewForSprite.setY(updatedSprite.getNewY());
	}

	private void registerNewlyGeneratedSprites(Update update) {
		update.getNewSpritesList().forEach(newSprite -> {
			newImageViews.add(idsToImageViews.get(addNewSpriteToDisplay(newSprite)));
		});
	}

	private void registerNewlyDeletedSprites(Update update) {
		update.getSpriteDeletionsList().forEach(deletedSprite -> {
			deletedImageViews.add(removeDeadSpriteFromDisplay(deletedSprite));
		});
	}

}