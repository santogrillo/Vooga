package engine.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import engine.SpriteQueryHandler;
import engine.behavior.collision.CollisionHandler;
import engine.behavior.movement.StationaryMovementStrategy;
import engine.game_elements.GameElement;
import javafx.geometry.Point2D;

public class SpriteQueryingTesting {
	SpriteQueryHandler queryHandler;
	ArrayList<GameElement> gameElements;
	GameElement target;
	
	public SpriteQueryingTesting() {
		queryHandler = new SpriteQueryHandler();
		gameElements = new ArrayList<>();
		target = createElement(100,0);
	}
	
	private void populateList(int numElements) {
		int x = 100;
		int y = 5;
		int increment = 10;
		for(int i=0;i<numElements;i++) {
			gameElements.add(createElement(x,y));
			y += increment;
		}
	}
	
	private GameElement createElement(double x, double y) {
		GameElement element = new GameElement("element", null,new StationaryMovementStrategy(new Point2D(x,y)),null);
		element.setX(x); 
		element.setY(y);
		return element;
	}
	
	@Test 
	public void testQueryHandler() {
		populateList(50);
		int range = 500;
		List<GameElement> withinRange = queryHandler.getAllElementsWithinRange(0, new Point2D(target.getX(),target.getY()),
				gameElements, range);
		assertEquals("Test range querying",range/10,withinRange.size());
	}
}
