package at.bestsolution.sample.code;

import org.eclipse.fx.code.editor.Constants;
import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.SourceFileChange;
import org.eclipse.fx.code.editor.StringInput;
import org.eclipse.fx.code.editor.fx.TextEditor;
import org.eclipse.fx.code.editor.fx.services.internal.DefaultSourceViewerConfiguration;
import org.eclipse.fx.code.editor.services.InputDocument;
import org.eclipse.fx.core.event.Event;
import org.eclipse.fx.core.event.EventBus;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.sample.code.complete.DartProposalComputer;
import at.bestsolution.sample.code.generated.DartPartitioner;
import at.bestsolution.sample.code.generated.DartPresentationReconciler;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public class DartEditor extends TextEditor {
	private ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper(this, "modified",false);

	private final StringInput input;

	private DartProposalComputer dartProposalComputer;

	public DartEditor(StringInput input, EventBus eventBus, DartServer server) {
		this.input = input;
		setInput(input);
		setDocument(new InputDocument(input));
		setPartitioner(new DartPartitioner());
		dartProposalComputer = new DartProposalComputer(server);
		setSourceViewerConfiguration(new DefaultSourceViewerConfiguration(input, new DartPresentationReconciler(), dartProposalComputer, null, null));
		eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_MODIFIED, this::handleModified);
		eventBus.subscribe(Constants.TOPIC_SOURCE_FILE_INPUT_SAVED, this::handleSaved);
	}

	private void handleModified(Event<SourceFileChange> e) {
		if( e.getData().input == this.input ) {
			modified.set(true);
		}
	}

	private void handleSaved(Event<Input<?>> e) {
		if( e.getData() == this.input ) {
			modified.set(false);
		}
	}

	public final javafx.beans.property.ReadOnlyBooleanProperty modifiedProperty() {
		return this.modified.getReadOnlyProperty();
	}


	public final boolean isModified() {
		return this.modifiedProperty().get();
	}

	public void dispose() {
		dartProposalComputer.dispose();
	}
}
