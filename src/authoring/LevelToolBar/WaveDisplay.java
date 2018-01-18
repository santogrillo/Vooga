package authoring.LevelToolBar;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class WaveDisplay extends TabPane {
	
	private TabInterface tabInterface;
	private final String TAB_TEXT = "wave";
	
	public WaveDisplay(TabInterface tabInterface) {
		this.tabInterface = tabInterface;
	}

	public void addTabs(int numberOfWaves) {
		int mySize = this.getTabs().size();
		if (numberOfWaves < mySize) {
			this.getTabs().remove(numberOfWaves, mySize);
		} else {
			for (int i = mySize; i < numberOfWaves; i++) {
				Tab tab = new Tab();
				tab.setText(TAB_TEXT + String.valueOf(i+1));
				this.getTabs().add(tab);
				tab.setOnSelectionChanged(e -> tabInterface.updateImages());
				if (i == 0) {
					tab.setClosable(false);
				} else {
					tab.setOnClosed(e -> deleteWave(tab));
				}
			}
		}
	}

	private void deleteWave(Tab tab) {
		tabInterface.waveDeleted(this.getTabs().indexOf(tab));
		this.getTabs().remove(tab);
		for (int i = 0; i < this.getTabs().size(); i++) {
			this.getTabs().get(i).setText(TAB_TEXT + String.valueOf(i+1));
		}
	}

	public int getCurrTab() {
		return Math.min(this.getTabs().size(), 
				this.getSelectionModel().getSelectedIndex() + 1);
	}
}
