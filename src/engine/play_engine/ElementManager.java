package engine.play_engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.swing.plaf.synth.SynthSpinnerUI;

import engine.SpriteQueryHandler;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;
import engine.game_elements.GameElementFactory;
import factory.AudioClipFactory;

/**
 * Single-source of truth for elements and their behavior when in-play
 * 
 * @author radithya
 *
 */
public class ElementManager {

	private GameElementFactory gameElementFactory;
	// Use list to enforce an ordering of elements to facilitate consideration of
	// every element pair only once
	private List<GameElement> activeElements;
	private List<GameElement> newElements;
	private List<GameElement> updatedElements;
	private List<GameElement> deadElements;
	private Iterator<GameElement> waves;
	private GameElement currentWave;

	private final int FRAMES_BETWEEN_WAVES = 180;
	private int waveGapCountdown = 0;

	private AudioClipFactory audioClipFactory;

	private SpriteQueryHandler spriteQueryHandler;

	/**
	 * Handles the collision-checking and GameElement-specific collision-handling
	 * logic Implements the 'Behavior' interface from the api/doc in the
	 * DESIGN_PLAN.md
	 */
	public ElementManager(GameElementFactory gameElementFactory, SpriteQueryHandler spriteQueryHandler) {
		this.gameElementFactory = gameElementFactory;
		this.spriteQueryHandler = spriteQueryHandler;
		newElements = new ArrayList<>();
		updatedElements = new ArrayList<>();
		deadElements = new ArrayList<>();
		activeElements = new ArrayList<>();
	}

	// Guaranteed to return only active elements (i.e. not dead ones)
	Collection<GameElement> getCurrentElements() {
		// Filter to return only active elements?
		activeElements.removeIf(gameElement -> !gameElement.isAlive());
		return activeElements;
	}

	void setCurrentElements(List<GameElement> newElements) {
		activeElements = newElements;
	}
	
	void addElement(GameElement newElement) {
		activeElements.add(newElement);
	}
	
	void removeElement(GameElement elementToRemove) {
		activeElements.remove(elementToRemove);
	}

	void setCurrentWaves(List<GameElement> waves) {
		this.waves = waves.iterator();
		if (this.waves.hasNext()) {
			currentWave = this.waves.next();
		} else {
			currentWave = null;
		}
	}
	
	void update() {
		for (int elementIndex = 0; elementIndex < activeElements.size(); elementIndex++) {
			GameElement element = activeElements.get(elementIndex);
			element.move();
			handleElementFiring(element, false,false);
			processAllCollisionsForElement(elementIndex, element);
		}
		processWaveUpdate();
		activeElements.forEach(this::processStepForElement);
		activeElements.removeAll(deadElements);	
	}

	private void processWaveUpdate() {
		if (currentWave != null && waveGapCountdown <= 0) {
			handleElementFiring(currentWave, true,false);
			processStepForElement(currentWave);
			if (!currentWave.isAlive()) {
				if (waves.hasNext()) {
					waveGapCountdown = FRAMES_BETWEEN_WAVES;
					currentWave = waves.next();
				} else {
					currentWave = null;
				}
			}
		} else {
			waveGapCountdown--;
		}
	}

	private void processStepForElement(GameElement element) {
		if (!element.isAlive()) {
			if(element.shouldExplode()) {
				Map<String, Object> auxiliaryObjects = spriteQueryHandler.getAuxiliarySpriteConstructionObjectMap(new Point2D(element.getX(),element.getY()), element);
				try {
					GameElement explosionElement = gameElementFactory.generateElement(element.explode(), auxiliaryObjects);
					newElements.add(explosionElement);					
				} catch (ReflectiveOperationException failedToGenerateProjectileException) {
					// don't generate the projectile
					// TODO - throw exception? (prob not)
				}
			}
			deadElements.add(element);
		} else {
			updatedElements.add(element);
		}
	}

	List<GameElement> getNewlyGeneratedElements() {
		return newElements;
	}

	List<GameElement> getUpdatedElements() {
		return updatedElements;
	}
	
	List<GameElement> getDeadElements() {
		return deadElements;
	}

	void clearNewElements() {
		newElements.clear();
	}

	void clearUpdatedElements() {
		updatedElements.clear();
	}
	
	void clearDeadElements() {
		deadElements.clear();
	}

	boolean allEnemiesDead() {
		System.out.println("checking that all enemies are ded.");
		boolean result = allElementsFulfillCondition(element -> !element.isEnemy() || !element.isAlive());
		if (result) {
			System.out.println("WOAH");

		} else {
			System.out.println("expected");
		}
		return result;
	}

	boolean allAlliesDead() {
		return allElementsFulfillCondition(element -> !element.isAlly() || !element.isAlive());
	}

	boolean allWavesComplete() { return (!waves.hasNext() && currentWave == null && allEnemiesDead()); }
	

	boolean enemyReachedTarget() {
		return !allElementsFulfillCondition(element -> !element.isEnemy() || !element.reachedTarget());
	}

	boolean allElementsFulfillCondition(Predicate<GameElement> condition) {
		for (GameElement element : activeElements) {
			if (!condition.test(element)) {
				return false;
			}
		}
		return true;
	}

	private void processAllCollisionsForElement(int elementIndex, GameElement element) {
		for (int otherIndex = elementIndex + 1; otherIndex < activeElements.size(); otherIndex++) {
			GameElement otherElement = activeElements.get(otherIndex);
			if (element.collidesWith(otherElement)) {
				element.processCollision(getAllDamageAffectedElements(element,otherElement));
				otherElement.processCollision(getAllDamageAffectedElements(otherElement,element));
				playAudio(element.getCollisionAudio());
				playAudio(otherElement.getCollisionAudio());
			}
		}
	}
	
	private List<GameElement> getAllDamageAffectedElements(GameElement collider, GameElement collidee) {
		List<GameElement> exclusionOfSelf = getListOfElementsExcludingElement(collider);
		List<GameElement> allAffectedElements = spriteQueryHandler.
				getAllElementsWithinRange(collider.getPlayerId(), new Point2D(collider.getX(), collider.getY()), exclusionOfSelf, collider.getBlastRadius());
		if(!allAffectedElements.contains(collidee)) {
			allAffectedElements.add(collidee);
		}
		return allAffectedElements;
	}

	private void handleElementFiring(GameElement element, boolean isWave, boolean forcedFire) {
		final int UNREACHABLE_POINT = -1000;
		final Point2D DEFAULT_LOCATION= new Point2D( UNREACHABLE_POINT, UNREACHABLE_POINT);
		Point2D nearestTargetLocation = DEFAULT_LOCATION;
		GameElement nearestEnemyElement = getNearestEnemyElement(element);
		if(nearestEnemyElement != null) {
			nearestTargetLocation = new Point2D(nearestEnemyElement.getX(),nearestEnemyElement.getY());
		}
		String elementTemplateName;
		if (element.shouldFire(nearestTargetLocation.distance(element.getX(),element.getY())) 
							   && ((elementTemplateName = element.fire()) != null)
							   && (isWave || nearestTargetLocation!=DEFAULT_LOCATION)) {
			// Use player id of firing element rather than projectile? This allows greater flexibility
			Map<String, Object> auxiliaryObjects = spriteQueryHandler.getAuxiliarySpriteConstructionObjectMap(new Point2D(element.getX(),element.getY()),nearestEnemyElement);
			try {
				System.out.println("trying to shoot");
				GameElement projectileGameElement = gameElementFactory.generateElement(elementTemplateName, auxiliaryObjects);
				newElements.add(projectileGameElement);
			} catch (ReflectiveOperationException e) {
				
			}
			playAudio(element.getFiringAudio());
		}
		
	}
	
	private GameElement getNearestEnemyElement(GameElement element) {
		List<GameElement> exclusionOfSelf = removeStaticElementsFromList(
				getListOfElementsExcludingElement(element));
		return spriteQueryHandler.getNearestEnemy(
				element.getPlayerId(), new Point2D(element.getX(), element.getY()), exclusionOfSelf);
		
	}
	
	private List<GameElement> getListOfElementsExcludingElement(GameElement element){
		List<GameElement> exclusionOfSelf = new ArrayList<>(activeElements);
		exclusionOfSelf.remove(element);
		return exclusionOfSelf;
	}
	
	private List<GameElement> removeStaticElementsFromList(List<GameElement> list) {
		List<GameElement> result = new ArrayList<GameElement>(list);
		List<GameElement> statics = new ArrayList<GameElement>();
		for(GameElement e : list)
		{
			if(e.getPlayerId() == 0) { statics.add(e); }
		}
		result.removeAll(statics);
		return result;
	}
	
	private void playAudio(String audioUrl) {
		if(audioUrl != null) {
			audioClipFactory = new AudioClipFactory(audioUrl);
			audioClipFactory.getAudioClip().play();
		}
	}
	
	void triggeredFire(GameElement element) {
		this.handleElementFiring(element, false, true);
	}

}
