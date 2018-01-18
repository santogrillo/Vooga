package authoring.LevelToolBar;

import authoring.EditDisplay;
import authoring.GameArea;
import authoring.ScrollableArea;
import authoring.levelEditor.LevelsEditDisplay;
import display.factory.TabFactory;
import display.sprites.InteractiveObject;
import engine.AuthoringModelController;
import javafx.geometry.Point2D;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import networking.protocol.PlayerServer.NewSprite;
import util.ElementDefaultsGetter;
import util.protocol.ClientMessageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 
 * @author venkat, moboyle
 *
 */
public class LevelToolBar extends VBox implements TabInterface, ButtonInterface {
    
    private static final String DEFAULT_WAVE_PROPERTIES = "WavesDefaults";
    private final String UNTITLED = "untitled";
    private static final int SIZE = 400;
    private static final int WIDTH = 100;
    private static final int X_LAYOUT = 260;
    private static final int Y_LAYOUT = 470;
    private static final int STARTING_LEVEL = 0;
    private static final int LEVEL_INDEX = 0;
    private static final int WAVE_INDEX = 1;
    private static final int USER_OFFSET = 1;
    private static final int X_LOCATION = 0;
    private static final int Y_LOCATION = 1;
	private static final int HEIGHT = 400;

    private AuthoringModelController myController;
    private TabPane myTabPane;
    private ScrollableArea myScrollableArea;
    private WaveDisplay myWaveDisplay;
    private TabFactory tabMaker;
    private int currentLevel;
    private EditDisplay myCreated;
    private SpriteDisplayer mySpriteDisplay;
    private LevelsEditDisplay myLevelDisplayer;
    private Map<String, Object> myProperties;
    private List<String> elementsToSpawn;
    private int startingLevels;
    private Map<Integer, LevelData> levelToData;
    private ClientMessageUtils clientMessageUtils;
    private Point2D location;

    public LevelToolBar(EditDisplay created, AuthoringModelController controller, ScrollableArea area) {
        levelToData = new TreeMap<>();
        myScrollableArea = area;
        currentLevel = STARTING_LEVEL;
        myCreated = created;
        myController = controller;
        clientMessageUtils = new ClientMessageUtils();
        this.setLayoutX(X_LAYOUT);
        this.setLayoutY(Y_LAYOUT);
        this.setWidth(SIZE);
        this.setHeight(HEIGHT);
        myTabPane = new TabPane();
        tabMaker = new TabFactory();
        mySpriteDisplay = new SpriteDisplayer();
        myWaveDisplay = new WaveDisplay(this);
        this.getChildren().add(myWaveDisplay);
        this.getChildren().add(mySpriteDisplay);
        myTabPane.setMaxSize(SIZE, WIDTH);
        myTabPane.setPrefSize(SIZE, WIDTH);
        elementsToSpawn = new ArrayList<>();
        this.getChildren().add(myTabPane);
        loadLevels();
        created.setGameArea(levelToData.get(1).myGameArea);
        createProperties();
        myLevelDisplayer = new LevelsEditDisplay(myController);
    }

    private void createProperties() {
        myProperties = new ElementDefaultsGetter(DEFAULT_WAVE_PROPERTIES).getDefaultProperties();
    }

    @Override
    public void makeNewWave() {
        levelToData.get(currentLevel).addWave();
        updateWaveDisplay();
    }

    @Override
    public void openLevelDisplay() {
        myLevelDisplayer = new LevelsEditDisplay(myController);
        myLevelDisplayer.open();
    }

	private void loadLevels() {
		startingLevels = myController.getNumLevelsForGame();
		if (myController.getGameName().equals(UNTITLED) || startingLevels == 1) {
			addLevel();
			return;
		}
		for (int i = 1; i <= startingLevels; i++) {
			addLevel();
			initializeSprites(i);
		}
	}
	
	@Override
	public void addLevel() {
		myController.setLevel(levelToData.size()+USER_OFFSET); 
		/**
		 * Change the below level
		 */
		levelToData.put(levelToData.size()+USER_OFFSET, new LevelData(levelToData.size(), myController));
		Tab newTab = tabMaker.buildTabWithoutContent("Level " + Integer.toString(levelToData.size()), null, myTabPane);
		newTab.setContent(mySpriteDisplay);
		LevelTab newLv = new LevelTab(levelToData.size(), myController);
		if (levelToData.size() == 0) {
			newTab.setClosable(false);
		} else {
			newTab.setOnClosed(e -> deleteLevel(newLv.getLvNumber()));
		}
		newTab.setOnSelectionChanged(e -> changeDisplay(newLv.getLvNumber()));
		newLv.attach(newTab);
		levelToData.get(levelToData.size()).myLevelTab = newLv;
		myTabPane.getTabs().add(newTab);
	}
	
	// TODO need load in static object rather than just imageview
	private void initializeSprites(int level) {
		try {
			clientMessageUtils
					.initializeLoadedLevel(myController.loadOriginalGameState(myController.getGameName(), level));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("YA");
		int numWaves = myController.getNumWavesForLevel(level);
		System.out.println(numWaves);
		for (int i = 0; i < numWaves; i++) {
			System.out.println(myController.getWaveProperties(i).toString());
		}
		reloadSavedInventory(level);
		reloadSavedWaves(level);
	}
	
	private void reloadSavedWaves(int level) {
		List<String> imageStringList = (List<String>) myController.getWaveProperties(level).get("Elements to fire");
		System.out.println(imageStringList.toString());
		List<String> myImages = new ArrayList<>();
		List<String> uniqueImageString = (List<String>) imageStringList.stream().distinct().collect(Collectors.toList());
		List<Integer> imageCount = new ArrayList<>();
		List<ImageView> myImageViews = new ArrayList<>();
		for (String s : uniqueImageString) {
			imageCount.add(Collections.frequency(imageStringList, s));
			String imageString = (String) myController.getAllDefinedTemplateProperties().get(s).get("Path of game element image");
			myImages.add(imageString);
			Image myImage = new Image(imageString);
			ImageView myImageView = new ImageView(myImage);
			myImageView.setId(imageString);
			System.out.println(myController.getAllDefinedTemplateProperties().get(s).toString());
			myImageView.setFitHeight((Integer) myController.getAllDefinedTemplateProperties().get(s).get("Height"));
			myImageView.setFitWidth((Integer) myController.getAllDefinedTemplateProperties().get(s).get("Width"));
			myImageViews.add(myImageView);
		}
		mySpriteDisplay.addToScroll(myImageViews, imageCount);
	}

	private void reloadSavedInventory(int level) {
		for (Integer id : myController.getLevelSprites(level).stream().map(NewSprite::getSpriteId).collect(Collectors.toList())) {
			ImageView imageView = clientMessageUtils.getRepresentationFromSpriteId(id);
			InteractiveObject savedObject = new InteractiveObject(myCreated, imageView.getImage().toString());
			savedObject.setElementId(id);
			savedObject.setX(imageView.getX());
			savedObject.setY(imageView.getY());
			savedObject.setImageView(imageView);
			levelToData.get(level).myGameArea.addBackObject(savedObject);
		}
	}
	
	public void addToWave(String levelAndWave, String stringLocation, int amount, ImageView mySprite) {
		String[] levelWaveArray = levelAndWave.split("\\s+");
		String mySpriteId = mySprite.getId();
		List<ImageView> imageList = new ArrayList<>(Collections.nCopies(amount, mySprite));
		elementsToSpawn = new ArrayList<>(Collections.nCopies(amount, mySpriteId));
//		elementsToSpawn = imageList.stream().map(ImageView::getId).collect(Collectors.toList());
        if (stringLocation.split(",").length != 2) {
            location = new Point2D(100, 100);
        } else {
            String[] splitLocation = stringLocation.split(",");
            location = new Point2D(Integer.valueOf(splitLocation[X_LOCATION]),
                    Integer.valueOf(splitLocation[Y_LOCATION]));
        }
        Map<String, Object> waveProperties = new HashMap<>();
        waveProperties.putAll(myProperties);
        waveProperties.put("Elements to fire", elementsToSpawn);
        waveProperties.put("Projectile Type Name", mySpriteId);
        for (String levelDotWave : levelWaveArray) {
            int level = Integer.valueOf(levelDotWave.split("\\.+")[LEVEL_INDEX]);
            int wave = Integer.valueOf(levelDotWave.split("\\.+")[WAVE_INDEX]);
            myController.setLevel(level);
            if (levelToData.get(level) != null && levelToData.get(level).waveInfo.get(wave).waveId != null) {
                List<String> waveElements = levelToData.get(level).waveInfo.get(wave).spriteNames.stream().map(ImageView::getId).collect(Collectors.toList());
                waveElements.addAll(elementsToSpawn);
                waveProperties.put("Elements to fire", waveElements);
                myController.editWaveProperties(levelToData.get(level).waveInfo.get(wave).waveId,
                        waveProperties, waveElements, location);
                //TODO: Refactor code below for changing map
                List<ImageView> tempArray = new ArrayList<>();
                tempArray.addAll(levelToData.get(level).waveInfo.get(wave).spriteNames);
                tempArray.addAll(imageList);
                levelToData.get(level).waveInfo.get(wave).spriteNames = tempArray;
            } else {
                levelToData.get(level).waveInfo.get(wave).spriteNames = imageList;
                levelToData.get(level).waveInfo.get(wave).waveId =
                        myController.createWaveProperties(waveProperties, elementsToSpawn, location);
            }
            levelToData.get(level).waveInfo.get(wave).numberList.add(amount);
        }
        updateImages();
    }

    private void deleteLevel(int lvNumber) {
        myController.deleteLevel(lvNumber);
        Map<Integer, LevelData> tempMap = new TreeMap<Integer, LevelData>();
        levelToData.keySet().stream().forEach(waveKey -> {
            if (currentLevel < lvNumber) tempMap.put(waveKey, levelToData.get(waveKey));
            if (currentLevel > lvNumber) {
                tempMap.put(waveKey - 1, levelToData.get(waveKey));
            }
        });
        levelToData = tempMap;
//		myLevels.remove(lvNumber - 1);
//		myGameAreas.remove(lvNumber - 1);
//		for (int i = lvNumber - 1; i < myLevels.size(); i++) {
//			myLevels.get(i).decrementLevel();
//			myTabPane.getTabs().get(i).setText("Level " + Integer.toString(i + 1));
//		}
//		waveToData = updateDataMap(lvNumber);
    }

//	public Map<String,Data> updateDataMap(int levelRemoved) {
//		Map<String, Data> tempMap = new TreeMap<String, Data>();
//		waveToData.keySet().stream().forEach(waveKey -> {
//			int level = Integer.valueOf(waveKey.split("\\.+")[0]);
//			int wave = Integer.valueOf(waveKey.split("\\.+")[1]);
//			if (level < levelRemoved) tempMap.put(waveKey, waveToData.get(waveKey));
//			if (level > levelRemoved) {
//				tempMap.put(waveAndLevel(level-1, wave),
//						waveToData.get(waveKey));
//			}
//		});
//		return tempMap;
//	}

//	public Map<String,Data> updateDataMap(int levelRemoved, int waveRemoved) {
//		Map<String, Data> tempMap = new TreeMap<String, Data>();
//		waveToData.keySet().stream().forEach(waveKey -> {
//			int level = Integer.valueOf(waveKey.split("\\.+")[0]);
//			int wave = Integer.valueOf(waveKey.split("\\.+")[1]);
//			//Check this logic
//			if (level != levelRemoved || wave < levelRemoved) tempMap.put(waveKey, waveToData.get(waveKey));
//			else if (wave > levelRemoved) {
//				tempMap.put(waveAndLevel(level, wave-1), waveToData.get(waveKey));
//			}
//		});
//		wavesPerLevel.put(levelRemoved, wavesPerLevel.get(levelRemoved)-1);
//		return tempMap;
//	}


    private void updateWaveDisplay() {
        myWaveDisplay.addTabs(levelToData.get(currentLevel).waveInfo.size());
        updateImages();
    }

    public void changeDisplay(int i) {
        currentLevel = i;
        myScrollableArea.changeLevel(levelToData.get(i).myGameArea);
        myCreated.setDroppable(levelToData.get(i).myGameArea);
        myController.setLevel(i);
        myCreated.setGameArea(levelToData.get(i).myGameArea);
        updateWaveDisplay();
        updateImages();
    }

    public void updateImages() {
        mySpriteDisplay.clear();
        if (levelToData.get(currentLevel) != null) {
            mySpriteDisplay.addToScroll(levelToData.get(currentLevel).waveInfo.get(myWaveDisplay.getCurrTab()).spriteNames,
                    levelToData.get(currentLevel).waveInfo.get(myWaveDisplay.getCurrTab()).numberList);
        }
    }


    public int getMaxLevel() {
        return levelToData.size() - 1; //Maybe not -1 idk with this new indexing thing how we're counting lol
    }

    public void addLevelProperties(ImageView currSprite, int level) {
        levelToData.get(level).myLevelTab.update(currSprite);
    }
    //Not used?

    @Override
    public void waveDeleted(int waveNumber) {
        // TODO Auto-generated method stub

    }
}

class Data {
    List<ImageView> spriteNames;
    Integer waveId;
    List<Integer> numberList;

    Data() {
        spriteNames = new ArrayList<ImageView>();
        numberList = new ArrayList<Integer>();
    }
} 


class LevelData {
    Map<Integer, Data> waveInfo;
    GameArea myGameArea;
    LevelTab myLevelTab;
    AuthoringModelController myController;

    LevelData(int level, AuthoringModelController myController) {
        myLevelTab = new LevelTab(level, myController);
        waveInfo = new TreeMap<Integer, Data>();
        myGameArea = new GameArea(myController);
        waveInfo.put(1, new Data());
        this.myController = myController;
    }

    public void addWave() {
        waveInfo.put(waveInfo.size() + 1, new Data());

    }

    public void changeTab(LevelTab newTab) {
        myLevelTab = newTab;
    }
}