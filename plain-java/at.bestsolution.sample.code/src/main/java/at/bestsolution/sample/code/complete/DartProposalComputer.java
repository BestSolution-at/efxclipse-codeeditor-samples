package at.bestsolution.sample.code.complete;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.fx.code.editor.fx.services.ProposalComputer;
import org.eclipse.fx.code.editor.services.URIProvider;
import org.eclipse.fx.ui.controls.image.AdornedGraphicNode;
import org.eclipse.fx.ui.controls.image.AdornedGraphicNode.Adornment;
import org.eclipse.fx.ui.controls.image.AdornedGraphicNode.Location;
import org.eclipse.fx.ui.controls.styledtext.StyledString;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import at.bestsolution.dart.server.api.DartServer;
import at.bestsolution.dart.server.api.Registration;
import at.bestsolution.dart.server.api.model.CompletionGetSuggestionsResult;
import at.bestsolution.dart.server.api.model.CompletionResultsNotification;
import at.bestsolution.dart.server.api.model.CompletionSuggestion;
import at.bestsolution.dart.server.api.model.CompletionSuggestionKind;
import at.bestsolution.dart.server.api.model.ElementKind;
import at.bestsolution.dart.server.api.services.ServiceCompletion;
import at.bestsolution.sample.code.DartEditor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DartProposalComputer implements ProposalComputer {
	private ServiceCompletion completionService;
	private Registration proposalRegistration;
	private String requestId;
	private CompletableFuture<List<ICompletionProposal>> future;
	private List<ICompletionProposal> completions = new ArrayList<>();

	private static Image PROPERTY_ADORNMENT = new Image(DartEditor.class.getResource("7/property.png").toExternalForm());
	private static Image FIELD_PRIVATE = new Image(DartEditor.class.getResource("16/field_private_obj.png").toExternalForm());
	private static Image FIELD_PUBLIC = new Image(DartEditor.class.getResource("16/field_public_obj.png").toExternalForm());
	private static Image METHOD_PRIVATE = new Image(DartEditor.class.getResource("16/methpri_obj.png").toExternalForm());
	private static Image METHOD_PUBLIC = new Image(DartEditor.class.getResource("16/methpub_obj.png").toExternalForm());


	public DartProposalComputer(DartServer server) {
		completionService = server.getService(ServiceCompletion.class);
		proposalRegistration = completionService.results(this::handleHandleResults);
	}

	private synchronized void handleHandleResults(CompletionResultsNotification notification) {
		if (requestId != null && requestId.equals(notification.getId())) {
			completions.addAll(Stream.of(notification.getResults()).map(e -> mapToCompletion(notification, e))
					.collect(Collectors.toList()));
			if (notification.getIsLast()) {
				List<ICompletionProposal> tmp = completions;
				completions = new ArrayList<>();
				if (!future.isDone()) {
					Collections.sort(tmp,
							(o1, o2) -> ((DartCompletionProposal) o1).compareTo((DartCompletionProposal) o2));
					future.complete(tmp);
				} else {
					System.err.println("Received informations after the last item has been sent");
				}
			}
		}
	}

	private DartCompletionProposal mapToCompletion(CompletionResultsNotification notification,
			CompletionSuggestion proposal) {
		StyledString s = new StyledString();

		ImageView baseImage;
		List<Adornment> adornments = new ArrayList<>();

		if (proposal.getKind() == CompletionSuggestionKind.KEYWORD) {
			baseImage = null;
			s.appendSegment(proposal.getCompletion(), "dart-element-name");
		} else if (proposal.getKind() == CompletionSuggestionKind.INVOCATION) {
			if (proposal.getElement() != null) {
				if (proposal.getElement().getKind() == ElementKind.FUNCTION
						|| proposal.getElement().getKind() == ElementKind.METHOD
						|| proposal.getElement().getKind() == ElementKind.GETTER
						|| proposal.getElement().getKind() == ElementKind.SETTER) {
					if (proposal.getElement().getKind() == ElementKind.GETTER) {
						s.appendSegment(proposal.getElement().getName() + " \u2192 " + proposal.getReturnType(),
								"dart-element-name");
						adornments.add(Adornment.create(Location.LEFT_TOP, new ImageView(PROPERTY_ADORNMENT)));
					} else {
						s.appendSegment(proposal.getElement().getName()
								+ (proposal.getElement().getParameters() == null ? "()"
										: proposal.getElement().getParameters())
								+ " \u2192 " + proposal.getReturnType(), "dart-element-name");
					}

					if (proposal.getDeclaringType() != null) {
						s.appendSegment(" \u2014 " + proposal.getDeclaringType(), "dart-type-info");
					}

					if ((proposal.getElement().getFlags() & 0x10) == 0x10) {
						baseImage = new ImageView(METHOD_PRIVATE);
					} else {
						baseImage = new ImageView(METHOD_PUBLIC);
					}
				} else if (proposal.getElement().getKind() == ElementKind.FIELD) {
					s.appendSegment(proposal.getElement().getName() + " \u2192 " + proposal.getReturnType(),
							"dart-element-name");

					if (proposal.getDeclaringType() != null) {
						s.appendSegment(" \u2014 " + proposal.getDeclaringType(), "dart-type-info");
					}

					if ((proposal.getElement().getFlags() & 0x10) == 0x10) {
						baseImage = new ImageView(FIELD_PRIVATE);
					} else {
						baseImage = new ImageView(FIELD_PUBLIC);
					}
				} else {
					s.appendSegment(proposal.getElement().getKind() + " => " + proposal.getCompletion(),
							"dart-element-name");
					baseImage = null;
				}
			} else {
				baseImage = new ImageView(METHOD_PUBLIC);
				s.appendSegment(proposal.getKind() + " => " + proposal.getCompletion(), "dart-element-name");
			}
		} else {
			baseImage = null;
			s.appendSegment(proposal.getCompletion(), "java-element-name");
		}

		Supplier<Node> supplier;
		if (baseImage != null) {
			if (adornments.isEmpty()) {
				supplier = () -> baseImage;
			} else {
				supplier = () -> new AdornedGraphicNode(baseImage, adornments.toArray(new Adornment[0]));
			}
		} else {
			supplier = () -> null;
		}

		DartCompletionProposal completetionProposal = new DartCompletionProposal(proposal.getRelevance(),
				proposal.getCompletion(), notification.getReplacementOffset(), notification.getReplacementLength(), s,
				supplier);
		return completetionProposal;
	}

	@Override
	public Future<List<ICompletionProposal>> compute(ProposalContext context) {
		URIProvider p = (URIProvider) context.input;
		Path file = Paths.get(java.net.URI.create(p.getURI().toString())).toAbsolutePath();

		CompletionGetSuggestionsResult result = completionService.getSuggestions(file.toString(), context.location);
		requestId = result.getId();

		future = new CompletableFuture<>();

		return future;
	}

	public void dispose() {
		proposalRegistration.dispose();
	}
}
