package display.factory;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabFactory {
	public Tab buildTab(String name, String className, Node content, TabPane pane) {
		Tab product = new Tab(name);
		product.setContent(content);
		product.setId(className);
		return product;
	}
	
	public Tab buildTabWithoutContent(String name, String className, TabPane pane) {
		Tab product = new Tab(name);
		product.setId(className);
		return product;
	}
}
