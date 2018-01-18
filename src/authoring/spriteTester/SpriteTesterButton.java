package authoring.spriteTester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import display.interfaces.TestingInterface;
import javafx.scene.control.Button;
import util.ElementDefaultsGetter;

public class SpriteTesterButton extends Button {
	
	private static String DEFAULT_IMAGE_URL = "https://users.cs.duke.edu/~rcd/images/rcd.jpg\"";

	
	public SpriteTesterButton(TestingInterface tester) {
		this.setLayoutY(700);
		this.setText("Play Game");
		this.setOnAction(e -> {
			Map<String, Object> fun = new ElementDefaultsGetter(this.getClass().getSimpleName())
					.getDefaultProperties();
			List<String> sprites = new ArrayList<>();
			sprites.add("Tank");
			tester.createTesterLevel(fun, sprites);

		});
	}


}
