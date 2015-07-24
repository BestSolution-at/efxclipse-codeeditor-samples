package at.bestsolution.sample.code;

import org.eclipse.fx.code.editor.SourceChange;
import org.eclipse.fx.code.editor.StringInput;
import org.eclipse.fx.code.editor.fx.TextEditor;
import org.eclipse.fx.code.editor.fx.services.internal.DefaultSourceViewerConfiguration;
import org.eclipse.fx.code.editor.services.InputDocument;

import at.bestsolution.sample.code.generated.DartPartitioner;
import at.bestsolution.sample.code.generated.DartPresentationReconciler;
import javafx.beans.property.ReadOnlyBooleanWrapper;

public class DartEditor extends TextEditor {
	private ReadOnlyBooleanWrapper modified = new ReadOnlyBooleanWrapper(this, "modified",false);

	public DartEditor(StringInput input) {
		setInput(input);
		setDocument(new InputDocument(input));
		setPartitioner(new DartPartitioner());
		setSourceViewerConfiguration(new DefaultSourceViewerConfiguration(input, new DartPresentationReconciler(), null, null, null));
	}

	@Override
	protected void documentModified(SourceChange event) {
		super.documentModified(event);
		this.modified.set(true);
	}

	@Override
	protected void documentSaved() {
		super.documentSaved();
		this.modified.set(false);
	}

	public final javafx.beans.property.ReadOnlyBooleanProperty modifiedProperty() {
		return this.modified.getReadOnlyProperty();
	}


	public final boolean isModified() {
		return this.modifiedProperty().get();
	}
}
