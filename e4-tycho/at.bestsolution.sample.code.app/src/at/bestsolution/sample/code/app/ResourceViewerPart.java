package at.bestsolution.sample.code.app;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.fx.code.editor.services.EditorOpener;
import org.eclipse.fx.core.Memento;
import org.eclipse.fx.ui.controls.filesystem.FileItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceEvent;
import org.eclipse.fx.ui.controls.filesystem.ResourceItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceTreeView;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;

@SuppressWarnings("restriction")
public class ResourceViewerPart {
	@Inject
	EditorOpener opener;

	private Path rootDirectory;

	private ResourceTreeView viewer;

	private DartServer server;

	@Inject
	public ResourceViewerPart(DartServer server) {
		this.server = server;
	}

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
			server.getService(ServiceAnalysis.class).setAnalysisRoots(new String[] {rootDirectory.toFile().getAbsolutePath()}, new String[0], null);
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
			server.getService(ServiceAnalysis.class).setAnalysisRoots(new String[] {rootDirectory.toFile().getAbsolutePath()}, new String[0], null);
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
