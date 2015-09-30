package at.bestsolution.sample.code.app;

import javax.annotation.PostConstruct;

import javafx.scene.text.Font;

public class HackLoader {
	@PostConstruct
	void init() {
		if( Font.getFontNames("Hack").isEmpty() ) {
			Font.loadFont(DartFileManager.class.getClassLoader().getResource("css/Hack-Regular.ttf").toExternalForm(), 10);
			Font.loadFont(DartFileManager.class.getClassLoader().getResource("css/Hack-Italic.ttf").toExternalForm(), 10);
			Font.loadFont(DartFileManager.class.getClassLoader().getResource("css/Hack-BoldItalic.ttf").toExternalForm(), 10);
			Font.loadFont(DartFileManager.class.getClassLoader().getResource("css/Hack-Bold.ttf").toExternalForm(), 10);
		}
	}
}
