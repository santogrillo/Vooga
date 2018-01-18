package authoring.LevelToolBar;

import java.util.List;

import authoring.GameArea;
import display.factory.TabFactory;
import engine.AuthoringModelController;
import engine.authoring_engine.AuthoringController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

public class SpriteDisplayTab extends HBox {
	
	private TabPane myTabPane;
	private TabFactory tabMaker;
	private int myWaves;
	private List<LevelTab> myWavesList;
	private SpriteDisplayer mySpriteDisplay;
	private AuthoringModelController controller;
	private int currentDisplay;
	private final int TAB_PANE_PREF_WIDTH = 400;
	private final int TAB_PANE_PREF_HEIGHT = 100;
	private final int INITIAL_DISPLAY = 1;
	private final int INITIAL_WAVES = 0;
	private final String LEVELS_TEXT = "Level ";
	

	public SpriteDisplayTab(AuthoringModelController myController) {
		currentDisplay = INITIAL_DISPLAY;

		controller = myController;
		myTabPane = new TabPane();
		tabMaker = new TabFactory();
		myWaves = INITIAL_WAVES;
		myTabPane.setMaxSize(TAB_PANE_PREF_WIDTH, TAB_PANE_PREF_HEIGHT);
		myTabPane.setPrefSize(TAB_PANE_PREF_WIDTH, TAB_PANE_PREF_HEIGHT);
		this.getChildren().add(myTabPane);
	}
		
	public void addLevel() {
		Tab newTab = tabMaker.buildTabWithoutContent(LEVELS_TEXT + Integer.toString(myWavesList.size() + 1), null, myTabPane);
		newTab.setContent(mySpriteDisplay);
		LevelTab newLv = new LevelTab(myWavesList.size() + 1, controller);
		controller.setLevel(myWavesList.size() + 1);
//		if (myWavesList.size() == 0) {
//			newTab.setClosable(false);
//		} else {
//			newTab.setOnClosed(e -> deleteLevel(newLv.getLvNumber()));
//		}
		newTab.setOnSelectionChanged(e->changeLevel(newLv.getLvNumber()));
		newLv.attach(newTab);
		myWavesList.add(newLv);
		myTabPane.getTabs().add(newTab);
	}
	
	public void changeLevel(int i) {
		currentDisplay = i;
	}
	
	

}
