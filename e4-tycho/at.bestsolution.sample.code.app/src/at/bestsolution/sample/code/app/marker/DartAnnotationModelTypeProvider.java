package at.bestsolution.sample.code.app.marker;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.services.AnnotationModelTypeProvider;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.osgi.service.component.annotations.Component;

@Component
public class DartAnnotationModelTypeProvider implements AnnotationModelTypeProvider {
	
	@Override
	public boolean test(Input<?> t) {
		return (t instanceof org.eclipse.fx.code.editor.services.URIProvider) && ((org.eclipse.fx.code.editor.services.URIProvider)t).getURI().lastSegment().endsWith(".dart");
	}

	@Override
	public Class<? extends IAnnotationModel> getType(Input<?> input) {
		return DartAnnotationModel.class;
	}

}
