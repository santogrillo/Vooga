//package authoring.LevelToolBar;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.stream.Collectors;
//import authoring.EditDisplay;
//import authoring.GameArea;
//import authoring.ScrollableArea;
//import display.factory.TabFactory;
//import display.sprites.InteractiveObject;
//import engine.authoring_engine.AuthoringController;
//import javafx.geometry.Point2D;
//import javafx.scene.control.Button;
//import javafx.scene.control.Tab;
//import javafx.scene.control.TabPane;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.VBox;
//import networking.protocol.PlayerServer.NewSprite;
//import util.protocol.ClientMessageUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.stream.Collectors;
//
//public class LevelToolBarNew extends VBox implements TabInterface, ButtonInterface {
//	private static final int SIZE = 400;
//	private static final int WIDTH = 100;
//	private static final int X_LAYOUT = 260;
//	private static final int Y_LAYOUT = 470;
//	private static final int STARTING_LEVEL = 0;
//	private static final int LEVEL_INDEX = 0;
//	private static final int WAVE_INDEX = 0;
//	private static final int USER_OFFSET = 1;
//
//	private AuthoringModelController myController;
//	private TabPane myTabPane;
////	private List<LevelTab> myLevels;
////	private List<GameArea> myGameAreas;
////	private List<List<ImageView>> mySprites;
//	private ScrollableArea myScrollableArea;
//	private WaveDisplay myWaveDisplay;
//	private TabFactory tabMaker;
//	private Button newLevel;
//	private Button editLevel;
//	private int currentLevel;
//	private EditDisplay myCreated;
//	private SpriteDisplayer mySpriteDisplay;
//	private LevelsEditDisplay myLevelDisplayer;
////	private Map<Integer, Integer> wavesPerLevel;
//	private Map<String, Object> myProperties;
//	private List<String> elementsToSpawn;
////	private Map<String, Data> waveToData;
////	private NewWaveButton myNewWaveButton;
//	private int startingLevels;
//	private Map<Integer, LevelData> levelToData;
//	private NewWaveButton myNewWaveButton;
//
//    private ClientMessageUtils clientMessageUtils;
//
//	public LevelToolBarNew(EditDisplay created, AuthoringModelController controller, ScrollableArea area) {
//		levelToData = new TreeMap<Integer, LevelData>();
//		levelToData.put(0, new LevelData(controller));
//		myScrollableArea = area;
//		currentLevel = STARTING_LEVEL;
//		myCreated = created;
//		myController = controller;
//		clientMessageUtils = new ClientMessageUtils();
//		this.setLayoutX(X_LAYOUT);
//		this.setLayoutY(Y_LAYOUT);
//		this.setWidth(SIZE);
////		mySprites = new ArrayList<>();
////		mySprites.add(new ArrayList<>());
//		/** 
//		 * NewLevel Button needs to change. Use ButtonFactory
//		 */
//		newLevel = new Button("New Level");
//		myNewWaveButton = new NewWaveButton(this);
//		newLevel.setOnAction(e -> addLevel());
//		myTabPane = new TabPane();
//		tabMaker = new TabFactory();
//		mySpriteDisplay = new SpriteDisplayer();
//		myWaveDisplay = new WaveDisplay(this);
//		this.getChildren().add(myWaveDisplay);
//		this.getChildren().add(mySpriteDisplay);
//		myTabPane.setMaxSize(SIZE, WIDTH);
//		myTabPane.setPrefSize(SIZE, WIDTH);
//		editLevel = new Button("Edit Level");
//		editLevel.setOnAction(e -> openLevelDisplay());
//		elementsToSpawn = new ArrayList<String>();
//		this.getChildren().add(myTabPane);
//		this.getChildren().add(newLevel);
//		this.getChildren().add(editLevel);
//		this.getChildren().add(myNewWaveButton);
//		loadLevels();
//		created.setGameArea(levelToData.get(0).myGameArea);
//		createProperties();
//	}
//
//	private void createProperties() {
//		/**
//		 * Just a way of hardcoding waves. Will eventually be put into properties file.
//		 * Should be able to set attack period, everything else should be given (image invisible)
//		 */
//		myProperties = new TreeMap<>();
//		myProperties.put("Collision effects", "Invulnerable to collision damage");
//		myProperties.put("Collided-with effects", "Do nothing to colliding objects");
//		myProperties.put("Move an object", "Object will stay at desired location");
//		myProperties.put("Firing Behavior", "Shoot various element types in a sequence");
//		myProperties.put("imageHeight", 40);
//		myProperties.put("imageWidth", 40);
//		myProperties.put("imageUrl", "monkey.png");
//		myProperties.put("Name", "myWave");
//		myProperties.put("tabName", "Troops");
//		myProperties.put("Range of tower", 50000);
//		myProperties.put("Attack period", 120);
//		myProperties.put("Firing Sound", "Sounds");
//		myProperties.put("Numerical \"team\" association", 0);
//		myProperties.put("period", 60);
//		myProperties.put("Number of troops to spawn", 10);
//		//Note: Templates to fire is set when the troop is selected
//	}
//	
//	@Override
//	public void makeNewWave() {
//		levelToData.get(currentLevel).addWave();
//		updateWaveDisplay();
//	}
//
//	@Override
//    public void openLevelDisplay() {
//        myLevelDisplayer = new LevelsEditDisplay(myController, myCreated);
//        myLevelDisplayer.open();
//    }
//
//	private void loadLevels() {
//		startingLevels = myController.getNumLevelsForGame(myController.getGameName(), true);
//		if (myController.getGameName().equals("untitled") || startingLevels == 0) {
//			addLevel();
//			return;
//		}
//		for (int i = 0; i < startingLevels; i++) {
//			addLevel();
//			initializeSprites(i);
//		}
//	}
//	
//	@Override
//	public void addLevel() {
////		mySprites.add(new ArrayList<>());
//		myController.setLevel(levelToData.size()); 
//		levelToData.put(levelToData.size(), new LevelData(myController));
//		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(levelToData.size()), null, myTabPane);
//		newTab.setContent(mySpriteDisplay);
//		LevelTab newLv = new LevelTab(levelToData.size(), myController);
//		/**
//		 * Make the tabs closeable later
//		 */
////		if (levelToData.size() == 0) {
////			newTab.setClosable(false);
////		} else {
////			newTab.setOnClosed(e -> deleteLevel(newLv.getLvNumber()));
////		}
//		newTab.setOnSelectionChanged(e -> changeDisplay(newLv.getLvNumber()));
//		newLv.attach(newTab);
//		levelToData.get(0).myLevelTab = newLv;
//		myTabPane.getTabs().add(newTab);
//	}
//	
//	// TODO need load in static object rather than just imageview
//	private void initializeSprites(int level) {
//		try {
//			clientMessageUtils
//					.initializeLoadedLevel(myController.loadOriginalGameState(myController.getGameName(), level));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		for (Integer id : myController.getLevelSprites(level).stream().map(NewSprite::getSpriteId).collect(Collectors.toList())) {
//			ImageView imageView = clientMessageUtils.getRepresentationFromSpriteId(id);
//			InteractiveObject savedObject = new InteractiveObject(myCreated, imageView.getImage().toString());
//			savedObject.setElementId(id);
//			savedObject.setX(imageView.getX());
//			savedObject.setY(imageView.getY());
//			savedObject.setImageView(imageView);
//			levelToData.get(level).myGameArea.addBackObject(savedObject);
//		}
//	}
//	
//	public void addToWave(String levelAndWave, int amount, ImageView mySprite) {
//		String[] levelWaveArray = levelAndWave.split("\\s+");
//		String mySpriteId = mySprite.getId();
//		List<ImageView> imageList = new ArrayList<>(Collections.nCopies(amount, mySprite));
//		elementsToSpawn = new ArrayList<>(Collections.nCopies(amount, mySpriteId));
////		elementsToSpawn = imageList.stream().map(ImageView::getId).collect(Collectors.toList());
//		Point2D location = new Point2D(30,60);
//		myProperties.put("templatesToFire", elementsToSpawn);
////		myProperties.put("Projectile Type Name", mySprite.getId());
//		Map<String, Object> waveProperties = new HashMap<>();
//        waveProperties.putAll(myProperties);
//        waveProperties.put("templatesToFire", elementsToSpawn);
//        waveProperties.put("Projectile Type Name", mySpriteId);
//		/**
//		 * Eventually we won't need line above, but for shoot periodically firing strategy
//		 * we have to include the projectile name that we're firing as a parameter. At the moment
//		 * the wave will only produce the last projectile that we add to it.
//		 * Also note that shoot periodically happens forever
//		 * Basically the elementsToSpawn is virtually useless with shoot periodically firing
//		 * strategy. Waiting for backend integration of round robin firing strategy
//		 */
//		for (String levelDotWave : levelWaveArray) {
//			int level = Integer.valueOf(levelDotWave.split("\\.+")[LEVEL_INDEX]) - USER_OFFSET;			
//			int wave = Integer.valueOf(levelDotWave.split("\\.+")[WAVE_INDEX]) - USER_OFFSET;
//			myController.setLevel(level);
//			if (levelToData.containsKey(level)) {
//				try {
//					List<String> waveElements = levelToData.get(level).waveInfo.get(wave).spriteNames.stream().map(ImageView::getId).collect(Collectors.toList());
//					waveElements.addAll(elementsToSpawn);
//					System.out.println(elementsToSpawn.toString());
//					waveProperties.put("templatesToFire", waveElements);
//					myController.editWaveProperties(levelToData.get(level).waveInfo.get(wave).waveId,
//							waveProperties, waveElements, location);
//				} catch (ReflectiveOperationException e) {
//					e.printStackTrace();
//				}
//				//TODO: Refactor code below for changing map
//				List<ImageView> tempArray = levelToData.get(level).waveInfo.get(levelDotWave).spriteNames;
//				tempArray.addAll(imageList);
//				levelToData.get(level).waveInfo.get(wave).spriteNames = tempArray;
//
////				waveToData.put(levelDotWave, new Data(tempArray, waveToData.get(levelDotWave).waveId));
//			} else {
//				levelToData.get(level).waveInfo.get(wave).waveId = 
//						myController.createWaveProperties(myProperties, elementsToSpawn, location);
//			}
//		}
//		updateImages();
//	}
//	
//	private String levelAndWave() {
//		return waveAndLevel(currentLevel, myWaveDisplay.getCurrTab());
//	}
//	
//	private String waveAndLevel(int currLevel, int currWave) {
//		return currLevel + "." + currWave;
//	}
//
////	private void deleteLevel(int lvNumber) {
////		myController.deleteLevel(lvNumber);
////		myLevels.remove(lvNumber - 1);
////		myGameAreas.remove(lvNumber - 1);
////		for (int i = lvNumber - 1; i < myLevels.size(); i++) {
////			myLevels.get(i).decrementLevel();
////			myTabPane.getTabs().get(i).setText("Level " + Integer.toString(i + 1));
////		}
////		waveToData = updateDataMap(lvNumber);
////	}
//
////	public Map<String,Data> updateDataMap(int levelRemoved) {
////		Map<String, Data> tempMap = new TreeMap<String, Data>();
////		waveToData.keySet().stream().forEach(waveKey -> {
////			int level = Integer.valueOf(waveKey.split("\\.+")[0]);
////			int wave = Integer.valueOf(waveKey.split("\\.+")[1]);
////			if (level < levelRemoved) tempMap.put(waveKey, waveToData.get(waveKey));
////			if (level > levelRemoved) {
////				tempMap.put(waveAndLevel(level-1, wave),
////						waveToData.get(waveKey));
////			}
////		});
////		return tempMap;
////	}
//	
////	public Map<String,Data> updateDataMap(int levelRemoved, int waveRemoved) {
////		Map<String, Data> tempMap = new TreeMap<String, Data>();
////		waveToData.keySet().stream().forEach(waveKey -> {
////			int level = Integer.valueOf(waveKey.split("\\.+")[0]);
////			int wave = Integer.valueOf(waveKey.split("\\.+")[1]);
////			//Check this logic
////			if (level != levelRemoved || wave < levelRemoved) tempMap.put(waveKey, waveToData.get(waveKey));
////			else if (wave > levelRemoved) {
////				tempMap.put(waveAndLevel(level, wave-1), waveToData.get(waveKey));
////			}
////		});
////		wavesPerLevel.put(levelRemoved, wavesPerLevel.get(levelRemoved)-1);
////		return tempMap;
////	}
//
//
//	private void updateWaveDisplay() {
//        myWaveDisplay.addTabs(levelToData.get(currentLevel).waveInfo.size());
//        updateImages();
//    }
//
//    public void changeDisplay(int i) {
//        currentLevel = i;
//        myScrollableArea.changeLevel(levelToData.get(i).myGameArea);
//        myCreated.setDroppable(levelToData.get(i).myGameArea);
//        myController.setLevel(i);
//        myCreated.setGameArea(levelToData.get(i).myGameArea);
//        updateWaveDisplay();
//        updateImages();
//    }
//
//    public void updateImages() {
//        mySpriteDisplay.clear();
//        if (levelToData.get(currentLevel) != null) {
//        	mySpriteDisplay.addToScroll(levelToData.get(currentLevel).waveInfo.get(myWaveDisplay.getCurrTab()).spriteNames);
//        }
//    }
//
//	@Override
//	public void waveDeleted(int waveNumber) {
////		waveToData = updateDataMap(currentLevel, waveNumber);
//	}
//
//	public int getMaxLevel() {
//		return levelToData.size()-1; //Maybe not -1 idk with this new indexing thing how we're counting lol
//    }
//
////    public void addLevelProperties(ImageView currSprite, int level) {
////        myLevels.get(level - 1).update(currSprite);
////    }
//	//Not used?
//}
//
////class Data {
////    List<ImageView> spriteNames;
////    Integer waveId;
////
////    Data(List<ImageView> spriteNames, Integer waveId) {
////        this.spriteNames = spriteNames;
////        this.waveId = waveId;
////    }
////} 
//
//
////
////class LevelData {
////	Map<Integer, Data> waveInfo;
////	GameArea myGameArea;
////	LevelTab myLevelTab;
////	AuthoringController myController;
////	
////	LevelData(AuthoringModelController myController) {
////		this.myController = myController;
////		waveInfo.put(0, new Data(new ArrayList<>(), null));
////		myGameArea = new GameArea(myController);
////	}
////	
////	public void addWave() {
////		waveInfo.put(waveInfo.size()+1, new Data(new ArrayList<>(), null));
////	}
////	
////	private void deleteWave(int waveNum) {
////		
////	}
////	
////	private boolean containsKey(int waveNum) {
////		return waveInfo.containsKey(waveNum);
////	}
////	
////	private Integer getWaves() {
////		return waveInfo.size();
////	}
////}