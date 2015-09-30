package at.bestsolution.sample.code.app.marker;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.fx.services.AnnotationPresenterTypeProvider;
import org.eclipse.jface.text.source.AnnotationPresenter;
import org.osgi.service.component.annotations.Component;

@Component
public class DartAnnotationPresenterTypeProvider implements AnnotationPresenterTypeProvider {

	@Override
	public Class<? extends AnnotationPresenter> getType(Input<?> s) {
		return DartAnnotationPresenter.class;
	}

	@Override
	public boolean test(Input<?> t) {
		return (t instanceof org.eclipse.fx.code.editor.services.URIProvider) && ((org.eclipse.fx.code.editor.services.URIProvider)t).getURI().lastSegment().endsWith(".dart");
	}

}
