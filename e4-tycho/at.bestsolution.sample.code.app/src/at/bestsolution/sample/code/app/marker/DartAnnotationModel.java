package at.bestsolution.sample.code.app.marker;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.fx.code.editor.Input;
import org.eclipse.fx.code.editor.services.URIProvider;
import org.eclipse.fx.ui.services.sync.UISynchronize;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.AnnotationModel;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.Registration;
import at.bestsolution.dart.server.api.model.AnalysisError;
import at.bestsolution.dart.server.api.model.AnalysisErrorsNotification;
import at.bestsolution.dart.server.api.model.AnalysisGetErrorsResult;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;

public class DartAnnotationModel extends AnnotationModel {
	private Registration subscription;
	private final UISynchronize synchronize;
	private Path file;

	@Inject
	public DartAnnotationModel(DartServer server, Input<?> input, UISynchronize synchronize) {
		this.synchronize = synchronize;
		URIProvider uriProvider = (URIProvider) input;
		file = Paths.get(URI.create(uriProvider.getURI().toString())).toAbsolutePath();

		// Subscribe to errors
		ServiceAnalysis service = server.getService(ServiceAnalysis.class);
		subscription = service.errors(this::accept);

		CompletableFuture.supplyAsync(
				() -> service.getErrors(file.toString())).thenAccept(this::accept);

	}

	private void accept(AnalysisErrorsNotification notification) {
		if( file.equals(Paths.get(notification.getFile()))) {
			processSubscriptions(notification.getErrors());
		}
	}

	private void accept(AnalysisGetErrorsResult result) {
		processSubscriptions(result.getErrors());
	}

	private void processSubscriptions(AnalysisError[] error) {
		synchronize.asyncExec(() -> {
			removeAllAnnotations();
			Stream.of(error).forEach(this::accept);
		});
	}

	private void accept(AnalysisError e) {
		addAnnotation(new DartAnnotation(e), new Position(e.getLocation().getOffset(), e.getLocation().getLength()));
	}

	@PreDestroy
	void cleanup() {
		if( subscription != null ) {
			subscription.dispose();
			subscription = null;
		}
	}
}
