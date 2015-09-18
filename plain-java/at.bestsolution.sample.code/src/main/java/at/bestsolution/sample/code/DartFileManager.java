package at.bestsolution.sample.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.LocalSourceFileInput;
import org.eclipse.fx.code.editor.SourceFileChange;
import org.eclipse.fx.core.event.Event;
import org.eclipse.fx.core.event.EventBus;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.model.AddContentOverlay;
import at.bestsolution.dart.server.api.model.ChangeContentOverlay;
import at.bestsolution.dart.server.api.model.RemoveContentOverlay;
import at.bestsolution.dart.server.api.model.SourceEdit;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;

public class DartFileManager {
	private List<String> activeInputList = new ArrayList<>();
	private Set<String> activeContentOverlay = new HashSet<>();
	private DartServer server;

	public DartFileManager(EventBus eventBroker, DartServer server) {
		this.server = server;
		eventBroker.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_CREATED, this::handleInputOpened);
		eventBroker.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_MODIFIED, this::handleInputModified);
		eventBroker.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_SAVED, this::handleInputSaved);
		eventBroker.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_DISPOSED, this::handleInputDisposed);
	}

	void handleInputModified(Event<SourceFileChange> e) {
		SourceFileChange modified = e.getData();
		String filePath = ((LocalSourceFileInput)modified.input).getPath().toAbsolutePath().toString();
		if( activeInputList.contains(filePath) ) {
			if(! activeContentOverlay.contains(filePath) ) {
				AddContentOverlay overlay = new AddContentOverlay();
				overlay.setContent(modified.input.getData());
				server.getService(ServiceAnalysis.class).updateContent(Collections.singletonMap(filePath, overlay));
				activeContentOverlay.add(filePath);
			} else {
				ChangeContentOverlay overlay = new ChangeContentOverlay();
				SourceEdit edit = new SourceEdit();
				edit.setOffset(modified.offset);
				edit.setLength(modified.length);
				edit.setReplacement(modified.replacement);
				overlay.setEdits(new SourceEdit[] { edit });
				server.getService(ServiceAnalysis.class).updateContent(Collections.singletonMap(filePath, overlay));
			}
		}
	}

	void handleInputSaved(Event<Input<?>> e) {
		LocalSourceFileInput input = (LocalSourceFileInput) e.getData();
		String filePath = input.getPath().toAbsolutePath().toString();
		if( activeContentOverlay.contains(filePath) ) {
			server.getService(ServiceAnalysis.class).updateContent(Collections.singletonMap(filePath, new RemoveContentOverlay()));
			activeContentOverlay.remove(filePath);
		}
	}

	void handleInputOpened(Event<Input<?>> e) {
		synchronized (activeInputList) {
			activeInputList.add(((LocalSourceFileInput)e.getData()).getPath().toAbsolutePath().toString());
		}
	}

	void handleInputDisposed(Event<Input<?>> e) {
		synchronized (activeInputList) {
			String filePath = ((LocalSourceFileInput)e.getData()).getPath().toAbsolutePath().toString();
			activeInputList.remove(filePath);
			if( activeContentOverlay.contains(filePath) ) {
				server.getService(ServiceAnalysis.class).updateContent(Collections.singletonMap(filePath, new RemoveContentOverlay()));
			}
		}
	}

}
