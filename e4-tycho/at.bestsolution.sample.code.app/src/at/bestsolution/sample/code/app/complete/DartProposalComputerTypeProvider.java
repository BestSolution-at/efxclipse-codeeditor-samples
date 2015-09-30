package at.bestsolution.sample.code.app.complete;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.fx.services.ProposalComputer;
import org.eclipse.fx.code.editor.fx.services.ProposalComputerTypeProvider;
import org.osgi.service.component.annotations.Component;

@Component
@SuppressWarnings("restriction")
public class DartProposalComputerTypeProvider implements ProposalComputerTypeProvider {

	@Override
	public Class<? extends ProposalComputer> getType(Input<?> s) {
		return DartProposalComputer.class;
	}

	@Override
	public boolean test(Input<?> t) {
		return (t instanceof org.eclipse.fx.code.editor.services.URIProvider) && ((org.eclipse.fx.code.editor.services.URIProvider)t).getURI().lastSegment().endsWith(".dart");
	}

}
