package at.bestsolution.dart.server.sample;

import java.io.IOException;
import java.util.stream.Stream;

import org.eclipse.fx.core.Util;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.DartServerFactory;
import at.bestsolution.dart.server.api.Registration;
import at.bestsolution.dart.server.api.model.CompletionResultsNotification;
import at.bestsolution.dart.server.api.services.ServiceAnalysis;
import at.bestsolution.dart.server.api.services.ServiceCompletion;

public class DartServerSample {
	public static void main(String[] args) {
		// Get the server factory from the service registry
		DartServerFactory serverFactory = Util.lookupService(DartServerFactory.class);
		// Create a server instance
		DartServer server = serverFactory.getServer("server");

		// Get the analysis and completion service
		ServiceAnalysis analysisService = server.getService(ServiceAnalysis.class);
		ServiceCompletion completionService = server.getService(ServiceCompletion.class);

		// set the root
		analysisService.setAnalysisRoots(new String[] {"/Users/tomschindl/dart-samples/"}, new String[0], null);
		// register for completion notifcations
		Registration proposalRegistration = completionService.results(DartServerSample::handleHandleResults);

		// Request completion at offset 367
		completionService.getSuggestions("/Users/tomschindl/dart-samples/test.dart", 367);

		// Wait for a key press
		try {
			System.in.read();
		} catch (IOException e) {
		}

		// unregister the notification listener
		proposalRegistration.dispose();
		// shutdown the server instance
		server.dispose();
	}

	private static void handleHandleResults(CompletionResultsNotification notification) {
		Stream.of(notification.getResults()).forEach( c -> System.err.println(c.getCompletion()));
	}
}
