package util.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import engine.game_elements.GameElement;
import networking.protocol.AuthorClient.AuthoringClientMessage;
import networking.protocol.AuthorClient.DefineElement;
import networking.protocol.AuthorClient.Property;
import networking.protocol.AuthorServer.AuxiliaryElementConfigurationOption;
import networking.protocol.AuthorServer.ConditionAssignment;
import networking.protocol.AuthorServer.DoubleProperty;
import networking.protocol.AuthorServer.ElementBaseConfigurationOption;
import networking.protocol.AuthorServer.ElementUpgrade;
import networking.protocol.AuthorServer.StringProperties;
import networking.protocol.AuthorServer.StringProperty;
import networking.protocol.PlayerServer.ElementCost;
import networking.protocol.PlayerServer.Inventory;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Resource;
import networking.protocol.PlayerServer.ResourceUpdate;
import networking.protocol.PlayerServer.SpriteDeletion;
import networking.protocol.PlayerServer.SpriteUpdate;
import networking.protocol.PlayerServer.StatusUpdate;
import networking.protocol.PlayerServer.TemplateProperties;
import networking.protocol.PlayerServer.TemplateProperty;
import networking.protocol.PlayerServer.Update;
import util.io.SerializationUtils;

public class ServerMessageUtils {

	private SerializationUtils serializationUtils = new SerializationUtils();

	public LevelInitialized packageState(Map<Integer, GameElement> levelSprites, Collection<String> inventory,
			Map<String, Double> resourceEndowments, int currentLevel) {
		return LevelInitialized
				.newBuilder().setSpritesAndStatus(packageUpdates(levelSprites, new HashMap<>(), new HashMap<>(), false,
						false, false, false, resourceEndowments, currentLevel))
				.setInventory(packageInventory(inventory)).build();
	}

	public Update packageStatusUpdate(boolean levelCleared, boolean isWon, boolean isLost, boolean inPlay,
			int currentLevel) {
		return Update.newBuilder().setStatusUpdates(getStatusUpdate(levelCleared, isWon, isLost, inPlay, currentLevel))
				.build();
	}

	public Inventory packageInventory(Collection<String> inventory) {
		Inventory.Builder inventoryBuilder = Inventory.newBuilder();
		inventory.forEach(template -> inventoryBuilder.addTemplates(template));
		return inventoryBuilder.build();
	}

	public Collection<TemplateProperties> packageAllTemplateProperties(
			Map<String, Map<String, String>> templateProperties) {
		return packageAllMessages(templateProperties.keySet(),
				templateName -> packageTemplateProperties(templateName, templateProperties.get(templateName)));
	}

	public Collection<ElementCost> packageAllElementCosts(Map<String, Map<String, Double>> elementCosts) {
		return packageAllMessages(elementCosts.keySet(),
				elementName -> packageElementCosts(elementName, elementCosts.get(elementName)));
	}

	private <R> Collection<R> packageAllMessages(Collection<String> messageDataSupplier,
			Function<String, R> messagePackager) {
		return messageDataSupplier.stream().map(messageData -> messagePackager.apply(messageData))
				.collect(Collectors.toSet());
	}

	public TemplateProperties packageTemplateProperties(String templateName,
			Map<String, String> templatePropertiesMap) {
		TemplateProperties.Builder templatePropertiesBuilder = TemplateProperties.newBuilder();
		templatePropertiesMap.keySet()
				.forEach(templateProperty -> templatePropertiesBuilder
						.addProperty(TemplateProperty.newBuilder().setName(templateProperty)
								.setValue(templatePropertiesMap.get(templateProperty)).build())
						.setElementName(templateName));
		return templatePropertiesBuilder.build();
	}

	public ElementCost packageElementCosts(String elementName, Map<String, Double> elementCosts) {
		ElementCost.Builder elementCostBuilder = ElementCost.newBuilder();
		elementCostBuilder.setElementName(elementName);
		elementCosts.keySet().forEach(resourceName -> elementCostBuilder.addCosts(
				Resource.newBuilder().setName(resourceName).setAmount(elementCosts.get(resourceName)).build()));
		return elementCostBuilder.build();
	}

	public Update packageUpdates(Map<Integer, GameElement> newSprites, Map<Integer, GameElement> updatedSprites,
			Map<Integer, GameElement> deletedSprites, boolean levelCleared, boolean isWon, boolean isLost,
			boolean inPlay, Map<String, Double> resourceEndowments, int currentLevel) {
		Update.Builder updateBuilder = Update.newBuilder();
		// Sprite Creations
		updateBuilder.addAllNewSprites(packageNewSprites(newSprites));
		// Sprite Updates
		updateBuilder.addAllSpriteUpdates(packageUpdatedSprites(updatedSprites));
		// Sprite Deletions
		updateBuilder.addAllSpriteDeletions(packageDeletedSprites(deletedSprites));
		// Status Updates
		updateBuilder.setStatusUpdates(getStatusUpdate(levelCleared, isWon, isLost, inPlay, currentLevel));
		// Resources - Just send all resources in update for now
		ResourceUpdate.Builder resourceUpdateBuilder = ResourceUpdate.newBuilder();
		resourceEndowments.keySet().forEach(resourceName -> resourceUpdateBuilder.addResources(
				Resource.newBuilder().setName(resourceName).setAmount(resourceEndowments.get(resourceName)).build()));
		return updateBuilder.setResourceUpdates(resourceUpdateBuilder.build()).build();
	}

	public Collection<NewSprite> packageNewSprites(Map<Integer, GameElement> newSprites) {
		return packageSprites(newSprites, (newSprite, newSpriteId) -> packageNewSprite(newSprite, newSpriteId));
	}

	public NewSprite packageNewSprite(GameElement newSprite, int spriteId) {
		return NewSprite.newBuilder().setSpriteId(spriteId).setImageURL(newSprite.getImageUrl())
				.setImageHeight(newSprite.getGraphicalRepresentation().getFitHeight())
				.setImageWidth(newSprite.getGraphicalRepresentation().getFitWidth()).setSpawnX(newSprite.getX())
				.setSpawnY(newSprite.getY()).build();
	}

	public SpriteUpdate packageUpdatedSprite(GameElement spriteToUpdate, int spriteId) {
		return SpriteUpdate.newBuilder().setSpriteId(spriteId).setNewX(spriteToUpdate.getX())
				.setNewY(spriteToUpdate.getY()).build();
	}

	public SpriteDeletion packageDeletedSprite(GameElement spriteToDelete, int spriteId) {
		return SpriteDeletion.newBuilder().setSpriteId(spriteId).build();
	}

	// AUTHORING SERVER - Consider moving to separate class?
	public Collection<ElementBaseConfigurationOption> packageElementBaseConfigurationOptions(
			Map<String, List<String>> configMap) {
		return configMap
				.entrySet().stream().map(entry -> ElementBaseConfigurationOption.newBuilder()
						.setConfigKey(entry.getKey()).addAllConfigOptions(entry.getValue()).build())
				.collect(Collectors.toList());
	}

	public Collection<AuxiliaryElementConfigurationOption> packageAuxiliaryElementConfigurationOptions(
			Map<String, Class> configMap) {
		return configMap
				.entrySet().stream().map(entry -> AuxiliaryElementConfigurationOption.newBuilder()
						.setConfigName(entry.getKey()).setConfigClassName(entry.getValue().getName()).build())
				.collect(Collectors.toList());
	}

	public Collection<ElementUpgrade> packageElementUpgrades(Map<String, List<Map<String, Object>>> upgradesMap) {
		return upgradesMap.entrySet().stream()
				.map(entry -> ElementUpgrade.newBuilder().setElementName(entry.getKey())
						.addAllElementUpgrades(entry.getValue().stream()
								.map(elementUpgrades -> StringProperties.newBuilder()
										.addAllItems(packageStringProperties(elementUpgrades)).build())
								.collect(Collectors.toList()))
						.build())
				.collect(Collectors.toList());
	}

	public Collection<DoubleProperty> packageResourceEndowments(Map<String, Double> resourceEndowments) {
		return resourceEndowments.entrySet().stream().map(resourceEntry -> DoubleProperty.newBuilder()
				.setName(resourceEntry.getKey()).setValue(resourceEntry.getValue()).build())
				.collect(Collectors.toList());
	}

	public Collection<StringProperty> packageWaveProperties(Map<String, Object> waveProperties) {
		return packageStringProperties(waveProperties);
	}

	public Collection<ConditionAssignment> packageConditionAssignments(Map<String, Collection<Integer>> conditions) {
		return conditions
				.entrySet().stream().map(condition -> ConditionAssignment.newBuilder()
						.setConditionName(condition.getKey()).addAllLevelsUsingCondition(condition.getValue()).build())
				.collect(Collectors.toList());
	}
	
	public DefineElement packageDefinedElement(String elementName, Map<String, Object> properties) {
		return DefineElement.newBuilder().setElementName(elementName)
				.addAllProperties(getPropertiesFromObjectMap(properties)).build();
	}
	
	public Collection<Property> getPropertiesFromObjectMap(Map<String, Object> objectMap) {
		Map<String, String> stringMap = serializationUtils.serializeElementTemplate(objectMap);
		return stringMap.entrySet().stream()
				.map(entry -> Property.newBuilder().setName(entry.getKey())
						.setValue(entry.getValue()).build())
				.collect(Collectors.toList());
	}

	//
	private StatusUpdate getStatusUpdate(boolean levelCleared, boolean isWon, boolean isLost, boolean inPlay,
			int currentLevel) {
		// Just always send status update for now
		return StatusUpdate.newBuilder().setLevelCleared(levelCleared).setIsWon(isWon).setIsLost(isLost)
				.setInPlay(inPlay).setCurrentLevel(currentLevel).build();
	}

	private Collection<SpriteUpdate> packageUpdatedSprites(Map<Integer, GameElement> updatedSprites) {
		return packageSprites(updatedSprites,
				(updatedSprite, updatedSpriteId) -> packageUpdatedSprite(updatedSprite, updatedSpriteId));
	}

	private Collection<SpriteDeletion> packageDeletedSprites(Map<Integer, GameElement> deadSprites) {
		return packageSprites(deadSprites,
				(deadSprite, deadSpriteId) -> packageDeletedSprite(deadSprite, deadSpriteId));
	}

	private <R> Collection<R> packageSprites(Map<Integer, GameElement> spriteMap,
			BiFunction<GameElement, Integer, R> spriteFunction) {
		return spriteMap.entrySet().stream()
				.map(spriteEntry -> spriteFunction.apply(spriteEntry.getValue(), spriteEntry.getKey()))
				.collect(Collectors.toList());
	}

	private Collection<StringProperty> packageStringProperties(Map<String, Object> stringProperties) {
		return serializationUtils.serializeElementTemplate(stringProperties).entrySet().stream()
				.map(elementUpgradeEntry -> StringProperty.newBuilder().setName(elementUpgradeEntry.getKey())
						.setValue(elementUpgradeEntry.getValue()).build())
				.collect(Collectors.toList());
	}


}
