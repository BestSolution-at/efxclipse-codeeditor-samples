package at.bestsolution.sample.code.app.editor.generated;

@org.osgi.service.component.annotations.Component
public class DartPresentationReconcilerTypeProvider implements org.eclipse.fx.code.editor.fx.services.PresentationReconcilerTypeProvider {

	@Override
	public Class<? extends org.eclipse.jface.text.presentation.PresentationReconciler> getType(org.eclipse.fx.code.editor.Input<?> s) {
		return at.bestsolution.sample.code.app.editor.generated.DartPresentationReconciler.class;
	}

	@Override
	public boolean test(org.eclipse.fx.code.editor.Input<?> t) {
		return (t instanceof org.eclipse.fx.code.editor.services.URIProvider) && ((org.eclipse.fx.code.editor.services.URIProvider)t).getURI().lastSegment().endsWith(".dart");
	}
}
