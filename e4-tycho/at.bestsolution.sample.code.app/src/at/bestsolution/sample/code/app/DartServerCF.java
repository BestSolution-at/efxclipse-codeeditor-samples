package at.bestsolution.sample.code.app;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.service.component.annotations.Component;

import at.bestsolution.dart.server.api.DartServerFactory;

@Component(service=IContextFunction.class,property="service.context.key:String=at.bestsolution.dart.server.api.DartServer")
public class DartServerCF extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context) {
		DartServerFactory factory = context.get(DartServerFactory.class);
		return factory.getServer("default");
	}
}
