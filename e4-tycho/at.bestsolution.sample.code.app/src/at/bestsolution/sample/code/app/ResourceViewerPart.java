package at.bestsolution.sample.code.app;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.fx.code.editor.services.TextEditorOpener;
import org.eclipse.fx.core.Memento;
import org.eclipse.fx.ui.controls.filesystem.FileItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceEvent;
import org.eclipse.fx.ui.controls.filesystem.ResourceItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceTreeView;

import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;

public class ResourceViewerPart {
	@Inject
	TextEditorOpener opener;

	private Path rootDirectory;

	private ResourceTreeView viewer;

	@PostConstruct
	void init(BorderPane parent, Memento memento) {
		viewer = new ResourceTreeView();

		if( rootDirectory == null ) {
			String dir = memento.get("root-dir", null);
			if( dir != null ) {
				rootDirectory = Paths.get(URI.create(dir));
			}
		}

		if( rootDirectory != null ) {
			viewer.setRootDirectories(FXCollections.observableArrayList(ResourceItem.createObservedPath(rootDirectory)));
		}
		viewer.addEventHandler(ResourceEvent.openResourceEvent(), this::handleOpenResource);
		parent.setCenter(viewer);
	}

	@Inject
	@Optional
	public void setRootDirectory(@Named("rootDirectory") Path rootDirectory) {
		this.rootDirectory = rootDirectory;
		if( viewer != null ) {
			viewer.setRootDirectories(FXCollections.observableArrayList(ResourceItem.createObservedPath(rootDirectory)));
		}
	}

	private void handleOpenResource(ResourceEvent<ResourceItem> e) {
		e.getResourceItems()
			.stream()
			.filter( r -> r instanceof FileItem)
			.map( r -> (FileItem)r)
			.filter( r -> r.getName().endsWith(".dart"))
			.forEach(this::handle);
	}

	private void handle(FileItem item) {
		opener.openEditor(item.getUri());
	}

	@PersistState
	public void rememberState(Memento memento) {
		if( rootDirectory != null ) {
			memento.put("root-dir", rootDirectory.toFile().toURI().toString());
		}
	}
}
