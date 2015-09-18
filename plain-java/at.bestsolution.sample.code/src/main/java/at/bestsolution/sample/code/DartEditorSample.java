package at.bestsolution.sample.code;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.fx.code.editor.LocalSourceFileInput;
import org.eclipse.fx.core.Util;
import org.eclipse.fx.core.event.EventBus;
import org.eclipse.fx.core.event.SimpleEventBus;
import org.eclipse.fx.ui.controls.filesystem.FileItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceEvent;
import org.eclipse.fx.ui.controls.filesystem.ResourceItem;
import org.eclipse.fx.ui.controls.filesystem.ResourceTreeView;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.DartServerFactory;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class DartEditorSample extends Application {

	private TabPane tabFolder;
	private ResourceTreeView viewer;
	private DartServer server;
	private EventBus eventBus = new SimpleEventBus();
	private DartFileManager fileManager;

	static class EditorData {
		final Path path;
		final DartEditor editor;

		public EditorData(Path path, DartEditor editor) {
			this.path = path;
			this.editor = editor;
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.server = getServer();
		this.fileManager = new DartFileManager(eventBus, server);
		BorderPane p = new BorderPane();
		p.setTop(createMenuBar());

		viewer = new ResourceTreeView();
		viewer.addEventHandler(ResourceEvent.openResourceEvent(), this::handleOpenResource);
		p.setLeft(viewer);

		tabFolder = new TabPane();
		p.setCenter(tabFolder);

		Scene s = new Scene(p, 800, 600);
		s.getStylesheets().add(getClass().getResource("default.css").toExternalForm());

		primaryStage.setScene(s);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		this.server.dispose();
		super.stop();
	}

	public DartServer getServer() {
		DartServerFactory serverFactory = Util.lookupService(DartServerFactory.class);
		return serverFactory.getServer("server");
	}

	private MenuBar createMenuBar() {
		MenuBar bar = new MenuBar();

		Menu fileMenu = new Menu("File");

		MenuItem rootDirectory = new MenuItem("Select root folder ...");
		rootDirectory.setOnAction(this::handleSelectRootFolder);

		MenuItem saveFile = new MenuItem("Save");
		saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S,KeyCombination.META_DOWN));
		saveFile.setOnAction(this::handleSave);


		fileMenu.getItems().addAll(rootDirectory, saveFile);

		bar.getMenus().add(fileMenu);

		return bar;
	}

	private void handleSelectRootFolder(ActionEvent e) {
		DirectoryChooser chooser = new DirectoryChooser();
		File directory = chooser.showDialog(viewer.getScene().getWindow());
		if( directory != null ) {
			viewer.setRootDirectories(
					FXCollections.observableArrayList(ResourceItem.createObservedPath(Paths.get(directory.getAbsolutePath()))));
			server.getService(ServiceAnalysis.class).setAnalysisRoots(new String[] {directory.getAbsolutePath()}, new String[0], null);

		}
	}

	private void handleSave(ActionEvent e) {
		Tab t = tabFolder.getSelectionModel().getSelectedItem();
		if( t != null ) {
			((EditorData)t.getUserData()).editor.save();
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
		Path path = (Path) item.getNativeResourceObject();

		Tab tab = tabFolder.getTabs().stream().filter( t -> ((EditorData)t.getUserData()).path.equals(path) ).findFirst().orElseGet(() -> {
			return createAndAttachTab(path, item);
		});
		tabFolder.getSelectionModel().select(tab);
	}

	private Tab createAndAttachTab(Path path, FileItem item) {
		BorderPane p = new BorderPane();
		DartEditor editor = new DartEditor(new LocalSourceFileInput(path, StandardCharsets.UTF_8, eventBus) {
			{ init(); }
		}, eventBus, server);
		editor.initUI(p);

		ReadOnlyBooleanProperty modifiedProperty = editor.modifiedProperty();
		StringExpression titleText = Bindings.createStringBinding(() -> {
			return modifiedProperty.get() ? "*" : "";
		}, modifiedProperty).concat(item.getName());

		Tab t = new Tab();
		t.textProperty().bind(titleText);
		t.setContent(p);
		t.setUserData(new EditorData(path, editor));
		t.setOnClosed(e -> {
			editor.dispose();
		});
		tabFolder.getTabs().add(t);
		return t;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
