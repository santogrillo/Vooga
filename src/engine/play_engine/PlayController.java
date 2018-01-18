package engine.play_engine;

import engine.AbstractGameController;
import engine.PlayModelController;
import engine.behavior.movement.LocationProperty;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import networking.protocol.PlayerServer.LevelInitialized;
import networking.protocol.PlayerServer.NewSprite;
import networking.protocol.PlayerServer.Update;
import util.GameConditionsReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Controls the model for a game being played. Allows the view to modify and
 * retrieve information about the model.
 *
 * @author radithya
 * @author Ben Schwennesen
 */
public class PlayController extends AbstractGameController implements PlayModelController {

	// The conditions don't take any arguments, at least for now
	private final Class[] CONDITION_METHODS_PARAMETER_CLASSES = new Class[] {};
	private final int HEALTH_LOSS_PER_ESCAPE = 5;
	
	private ElementManager elementManager;
	private GameConditionsReader conditionsReader;
	private boolean inPlay;
	private boolean isWon;
	private boolean isLost;
	private boolean levelCleared;
	private Method victoryConditionMethod;
	private Method defeatConditionMethod;
	private int maxLevels = DEFAULT_MAX_LEVELS;
	private List<Set<Entry<Integer, GameElement>>> savedList;
	private Update latestUpdate;

	private int score;
	private int cycles;

	public PlayController() {
		super();
		savedList = new ArrayList<>();
		elementManager = new ElementManager(getGameElementFactory(), getSpriteQueryHandler());
		conditionsReader = new GameConditionsReader();
		inPlay = true;
		latestUpdate = Update.getDefaultInstance();
		maxLevels = getNumLevelsForGame();
		System.out.println("Max levels: " + maxLevels);
		
	}

	@Override
	public LevelInitialized loadOriginalGameState(String saveName, int level) throws IOException {
		System.out.print("A");
		System.out.println(level);
		LevelInitialized levelData = super.loadOriginalGameState(saveName, level);
		updateForLevelChange(saveName, level);
		maxLevels = getNumLevelsForGame(saveName, true);
		System.out.println("Maxlevels: " + maxLevels);
		System.out.println("Current level: " + this.getCurrentLevel());
		return levelData;
	}

	@Override
	public LevelInitialized loadSavedPlayState(String savePlayStateName) throws FileNotFoundException {
		Collection<GameElement> oldGameElements = getLevelSprites().get(getCurrentLevel());
		// Get number of levels in play state
		int lastLevelPlayed = getNumLevelsForGame(savePlayStateName, false);
		// Load levels up to that level, as played (not original)
		for (int level = 1; level <= lastLevelPlayed; level++) {
			setLevel(level);
			loadLevelData(savePlayStateName, level, false);
		}
		updateForLevelChange(savePlayStateName, lastLevelPlayed);
		return packageStateChange(oldGameElements);
	}

	@Override
	public Update update() {
		if (inPlay) {
			/*---
			 * Uncomment when front end is ready to set wave properties fully (team & no. of
			 * attacks of wave)
			 */
			if (checkLevelClearanceCondition()) {
				if (checkVictoryCondition()) {
//					System.out.println("Victory Condition Fulfilled");
					registerVictory();
				} else {
//					System.out.println("Level Cleared Condition Fulfilled");
//					System.out.println("Max levels: " + maxLevels);
//					System.out.println("current level: " + getCurrentLevel());
				}
				registerLevelCleared();
			}
			if(this.checkDefeatCondition()) {
				this.registerDefeat();
			}
			/*
			 * } else if (checkDefeatCondition()) { registerDefeat(); } else {
			 */ // Move elements, check and handle collisions
			incrementCycles();
			savedList.add(getSpriteIdMap().entrySet());
			elementManager.update();
			List<GameElement> newlyGeneratedElements = elementManager.getNewlyGeneratedElements();
			List<GameElement> updatedElements = elementManager.getUpdatedElements();
			List<GameElement> deadElements = elementManager.getDeadElements();
			getLevelBanks().get(getCurrentLevel()).processPointsAndResourcesFromDeadElements(deadElements);
			for (GameElement element : newlyGeneratedElements) {
				cacheAndCreateIdentifier(element);
			}
			// Package these changes into an Update message
			latestUpdate = packageSpriteUpdates(newlyGeneratedElements, updatedElements, deadElements);
			getSpriteIdMap().entrySet().removeIf(entry -> deadElements.contains(entry.getValue()));
			deadElements.stream().filter(element -> element.reachedTarget() && element.isEnemy()).forEach(element -> decrementHealth(HEALTH_LOSS_PER_ESCAPE));
			elementManager.clearDeadElements();
			elementManager.clearNewElements();
			elementManager.clearUpdatedElements();
			return latestUpdate;
			// }
		}
		// If not in play, only one of the status properties could have changed, yes?
		return packageStatusUpdate();
	}
	
	private void decrementHealth(int amount) {
		int currentHealth = getLevelHealths().get(getCurrentLevel());
		getLevelHealths().set(getCurrentLevel(), currentHealth - amount);
	}

	@Override
	public void pause() {
		inPlay = false;
	}

	@Override
	public void resume() {
		inPlay = true;
		isWon = false;
		levelCleared = false;
	}

	@Override
	public boolean isLost() {
		return isLost;
	}

	@Override
	public boolean isWon() {
		return isWon;
	}

	@Override
	public Collection<NewSprite> getLevelSprites(int level) throws IllegalArgumentException {
		/*
		 * assertValidLevel(level); Collection<GameElement> levelGameElements =
		 * elementManager.getCurrentElements(); return
		 * getIdsCollectionFromSpriteCollection(levelGameElements);
		 */
		return null;
	}

	@Override
	public LocationProperty getElementLocationProperty(int elementId) throws IllegalArgumentException {
		if (!getSpriteIdMap().containsKey(elementId)) {
			throw new IllegalArgumentException();
		}
		return getSpriteIdMap().get(elementId).getLocationProperty();
	}

	@Override
	public NewSprite placeElement(String elementTemplateName, Point2D startCoordinates) {
		if (getLevelBanks().get(getCurrentLevel()).purchase(elementTemplateName, 1)) {
			// TODO - keep track of the resources that were changed in this cycle, and only
			// send them to client?
			return super.placeElement(elementTemplateName, startCoordinates);
		}
		// TODO - Custom Exception ?
		throw new IllegalArgumentException();
	}

	@Override
	public void upgradeElement(int elementId) throws IllegalArgumentException, ReflectiveOperationException {
		if (!getSpriteIdMap().containsKey(elementId)) {
			throw new IllegalArgumentException();
		}
		GameElement gameElement = getSpriteIdMap().get(elementId);
		elementManager.removeElement(gameElement);
		gameElement = getGameElementUpgrader().upgradeSprite(gameElement);
		elementManager.addElement(gameElement);
		getSpriteIdMap().put(elementId, gameElement);
		// I think this will update the reference in the element manager but might need
		// to manually
	}

	@Override
	public double getElementPointValue(int elementId) {
		if (!getSpriteIdMap().containsKey(elementId)) {
			return 0;
		}
		String elementName = getSpriteIdMap().get(elementId).getTemplateName();
		return getLevelBanks().get(getCurrentLevel()).getPointsValue(elementName);
	}

	@Override
	public boolean isLevelCleared() {
		return levelCleared;
	}

	@Override
	public boolean isReadyForNextLevel() {
		return isLevelCleared() && !isWon(); // For single-player, always ready if level cleared and not
	}

	public Update getLatestUpdate() {
		return latestUpdate;
	}

	public Update packageStatusUpdate() {
		return getServerMessageUtils().packageStatusUpdate(levelCleared, isWon, isLost, inPlay, getCurrentLevel());
	}
	
	//PlayModel controller to add to interface
		//PlayController has method that take int unique id return void. call manager that handles string return 
	
	public void triggerFire(int elementId) {
		elementManager.triggeredFire(this.getSpriteIdMap().get(elementId));

	}
	
	@Override
	public int getNumLevelsForGame(String gameName, boolean original) {
		try {
			return getIoController().getNumberOfLevelsForGame(gameName, original);
		} catch (FileNotFoundException e) {
			return getNumLevelsForGame();
		}
	}

	@Override
	protected void assertValidLevel(int level) throws IllegalArgumentException {
		// Enforce increments by at-most one for player
		if (level > getCurrentLevel() + 1) {
			throw new IllegalArgumentException();
		}
	}

	private void updateForLevelChange(String saveName, int level) {
		setLevel(level);
		//setMaxLevelsForGame(getNumLevelsForGame(saveName, true));
		elementManager.setCurrentElements(getLevelSprites().get(level));
		List<GameElement> levelWaves = getLevelWaves().get(getCurrentLevel());
		elementManager.setCurrentWaves(levelWaves);
		setVictoryCondition(getLevelConditions().get(level).get(VICTORY));
		setDefeatCondition(getLevelConditions().get(level).get(DEFEAT));
		resetScore();
		resetCycles();
	}

	private Update packageSpriteUpdates(Collection<GameElement> newlyGeneratedElements,
			Collection<GameElement> updatedElements, Collection<GameElement> deletedElements) {
		return getServerMessageUtils().packageUpdates(getFilteredSpriteIdMap(newlyGeneratedElements),
				getFilteredSpriteIdMap(updatedElements), getFilteredSpriteIdMap(deletedElements), levelCleared, isWon,
				isLost, inPlay, getResourceEndowments(), getCurrentLevel());
	}

	private void resetScore() {
		score = 0;
	}

	private void resetCycles() {
		cycles = 0;
	}

	private void incrementCycles() {
		cycles++;
	}

	private boolean checkVictoryCondition() {
		return getCurrentLevel() >= maxLevels;
	}

	private boolean checkDefeatCondition() {
		return dispatchBooleanMethod(defeatConditionMethod);
	}

	private boolean checkLevelClearanceCondition() {
		return dispatchBooleanMethod(victoryConditionMethod);
	}

	private boolean dispatchBooleanMethod(Method chosenBooleanMethod) {
		try {
			return (boolean) chosenBooleanMethod.invoke(this, new Object[] {});
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}

	private void registerVictory() {
		isWon = true;
		inPlay = false;
	}

	private void registerDefeat() {
		isLost = true;
		inPlay = false;
	}

	private void registerLevelCleared() {
		levelCleared = true;
		inPlay = false;
	}

	private void setMaxLevelsForGame(int maxLevels) {
		this.maxLevels = maxLevels;
	}

	private void setVictoryCondition(String conditionFunctionIdentifier) {
		victoryConditionMethod = getMethodForVictoryCondition(conditionFunctionIdentifier);
	}

	private void setDefeatCondition(String conditionFunctionIdentifier) {
		defeatConditionMethod = getMethodForDefeatCondition(conditionFunctionIdentifier);
	}

	private Method getMethodForVictoryCondition(String conditionFunctionIdentifier) throws IllegalArgumentException {
		String methodName = conditionsReader.getMethodNameForVictoryCondition(conditionFunctionIdentifier);
		return getMethodFromMethodName(methodName);
	}

	private Method getMethodForDefeatCondition(String conditionFunctionIdentifier) throws IllegalArgumentException {
		String methodName = conditionsReader.getMethodNameForDefeatCondition(conditionFunctionIdentifier);
		return getMethodFromMethodName(methodName);
	}

	private Method getMethodFromMethodName(String methodName) throws IllegalArgumentException {
		try {
			return this.getClass().getDeclaredMethod(methodName, CONDITION_METHODS_PARAMETER_CLASSES);
		} catch (NoSuchMethodException e) {
			// TODO - custom exception?
			throw new IllegalArgumentException();
		}
	}
	
	// TODO - Move conditions to separate file?

	// TODO (extension) - for multiplayer, take a playerId parameter in this method
	// and call for every playing playerId in game loop
	private boolean allEnemiesDead() {
		return elementManager.allEnemiesDead();
	}

	private boolean allWavesDead() {
		// return getLevelWaves().get(getCurrentLevel()).stream().filter(wave ->
		// wave.isAlive()).count() == 0;
		return elementManager.allWavesComplete();
	}

	// TODO - Boolean defeat conditions
	private boolean allAlliesDead() {
		return elementManager.allAlliesDead();
	}

	private boolean enemyReachedTarget() {
		return elementManager.enemyReachedTarget();
	}

	// TODO - Awarding of score in elementManager
	private boolean pointsQuotaReached() {
		return score >= getLevelPointQuotas().get(getCurrentLevel());
	}

	private boolean timeLimitReached() {
		return cycles >= getLevelTimeLimits().get(getCurrentLevel());
	}

	private boolean zeroHealth() {
		System.out.println("Checking for 0 health");
		return getLevelHealths().get(getCurrentLevel()) <= 0;
	}

}
