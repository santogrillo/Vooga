package display.toolbars;

import java.util.Map;

import display.tabs.SimpleTab;
import engine.AbstractGameModelController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * 
 * @author bwelton
 *
 */
public abstract class ToolBar extends VBox{
	private final double DEFAULT_WIDTH = 60;
	private final double DEFAULT_HEIGHT = 60;
	private final String HEIGHT = "Height";
	private final String WIDTH = "Width";
	private final String PATH = "Path of game element image";
	private final String TAB = "tabName";
	
	protected TabPane tabPane;
	
	protected void makeTabsUnclosable(TabPane pane) {
		for(int i = 0; i < pane.getTabs().size(); i++) {
			pane.getTabs().get(i).setClosable(false);
		}
	}
	
	public void addToToolbar(ImageView imageView, String tabName, TabPane pane) {
		for(Tab tab:pane.getTabs()) {
			if(tab.getText().equals(tabName)) {
				SimpleTab simpleTab = (SimpleTab) tab.getContent();
				simpleTab.addItem(imageView);
			}
		}
	}
	
	protected void initializeInventory(AbstractGameModelController controller, TabPane pane) {
		Map<String, Map<String, Object>> templates = controller.getAllDefinedTemplateProperties();
		for(String s:controller.getInventory()) {
			ImageView imageView;
			try {
				imageView = new ImageView(new Image(templates.get(s).get(PATH).toString()));
				
			}catch(NullPointerException e) {
				imageView = new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(templates.get(s)
						.get(PATH).toString())));
			}
			double height = DEFAULT_HEIGHT;
			double width = DEFAULT_WIDTH;
			try {
				System.out.println("Looking up inventory item " + s);
				height = Double.parseDouble(controller.getAllDefinedTemplateProperties().get(s).get(HEIGHT).toString());
				width = Double.parseDouble(controller.getAllDefinedTemplateProperties().get(s).get(WIDTH).toString());
			}catch(NumberFormatException e) {
				//add user warning about their image specifications
			}
			imageView.setFitHeight(height);
			imageView.setFitWidth(width);
			imageView.setId(s);
			imageView.setUserData(templates.get(s).get(PATH));
			addToToolbar(imageView, templates.get(s).get(TAB).toString(), pane);
		}
	}
	
	protected abstract void createAndAddTabs();

}
