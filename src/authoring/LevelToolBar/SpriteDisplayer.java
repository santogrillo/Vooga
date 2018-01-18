package authoring.LevelToolBar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class SpriteDisplayer extends ScrollPane {
	
	private final int BOX_PREF_WIDTH = 400;
	private final int BOX_PREF_HEIGHT = 75;
	private HBox myBox;
	
	public SpriteDisplayer() {
		myBox = new HBox();
		myBox.setPrefWidth(BOX_PREF_WIDTH);
		myBox.setPrefHeight(BOX_PREF_HEIGHT);
		myBox.setMaxHeight(BOX_PREF_HEIGHT);

		myBox.setStyle("-fx-background-color: #336699;");
		this.setContent(myBox);
	}
	
	public void addToScroll(List<ImageView> newSprites, List<Integer> num) {
		myBox.getChildren().clear();
		List<ImageView> uniqueSprites = newSprites.stream().distinct().collect(Collectors.toList());
		for (int i = 0; i < num.size(); i++) {
			HBox newOne = new HBox();
			newOne.getChildren().add(uniqueSprites.get(i));
			newOne.getChildren().add(new Label(String.valueOf(num.get(i))));
			myBox.getChildren().add(newOne);
		}
//		myBox.getChildren().addAll(new ArrayList<ImageView>(new HashSet<ImageView>(newSprites)));
	}

	public void clear() {
		myBox.getChildren().removeAll(myBox.getChildren());
	}
}
