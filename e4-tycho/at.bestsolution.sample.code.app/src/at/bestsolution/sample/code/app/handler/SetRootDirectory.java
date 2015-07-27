package at.bestsolution.sample.code.app.handler;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.fx.core.di.ContextValue;

import javafx.beans.property.Property;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SetRootDirectory {

	@Execute
	public void setRootDirectory(@ContextValue("rootDirectory") Property<Path> rootDirectory, Stage stage) {
		DirectoryChooser chooser = new DirectoryChooser();
		File directory = chooser.showDialog(stage);
		if( directory != null ) {
			rootDirectory.setValue(Paths.get(directory.getAbsolutePath()));
		}
	}
}
