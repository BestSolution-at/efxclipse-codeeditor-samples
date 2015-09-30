package at.bestsolution.sample.code.app.complete;

import java.util.function.Supplier;

import org.eclipse.jface.text.contentassist.CompletetionProposal;

import javafx.scene.Node;

@SuppressWarnings("restriction")
public class DartCompletionProposal extends CompletetionProposal implements Comparable<DartCompletionProposal> {
	private int relevance;

	public DartCompletionProposal(int relevance, String replacementString, int replacementOffset, int replacementLength,
			CharSequence label, Supplier<Node> graphicSupplier) {
		super(replacementString, replacementOffset, replacementLength, label, graphicSupplier);
	}

	@Override
	public int compareTo(DartCompletionProposal o) {
		int compare = Integer.compare(relevance, o.relevance);
		if( compare == 0 ) {
			compare = getLabel().toString().compareTo(o.getLabel().toString());
		}
		return compare;
	}
}
