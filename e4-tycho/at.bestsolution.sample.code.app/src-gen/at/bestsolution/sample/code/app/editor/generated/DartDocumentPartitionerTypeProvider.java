package at.bestsolution.sample.code.app.editor.generated;

@org.osgi.service.component.annotations.Component
public class DartDocumentPartitionerTypeProvider implements org.eclipse.fx.code.editor.services.DocumentPartitionerTypeProvider {
	@Override
	public Class<? extends org.eclipse.jface.text.IDocumentPartitioner> getType(org.eclipse.fx.code.editor.Input<?> s) {
		return at.bestsolution.sample.code.app.editor.generated.DartPartitioner.class;
	}

	public boolean test(org.eclipse.fx.code.editor.Input<?> t) {
		return (t instanceof org.eclipse.fx.code.editor.services.URIProvider) && ((org.eclipse.fx.code.editor.services.URIProvider)t).getURI().lastSegment().endsWith(".dart");
	}
}
