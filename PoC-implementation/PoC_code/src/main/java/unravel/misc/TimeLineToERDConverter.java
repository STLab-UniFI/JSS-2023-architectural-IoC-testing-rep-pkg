package unravel.misc;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.rpc.Call;

import timeLine.ContextualInstance;
import timeLine.InstanceMethod;
import timeLine.SessionTimeLine;
import timeLine.TimeLine;
import timeLine.TimeStep;
import unravel.cdi.Action;
import unravel.cdi.Scope;
import unravel.erd.Actor;
import unravel.erd.BoundaryObject;
import unravel.erd.CallController;
import unravel.erd.ContextController;
import unravel.erd.Controller;
import unravel.erd.EnrichedRobustnessDiagram;
import unravel.erd.Entity;
import unravel.erd.ErdBuilder;
import unravel.erd.RequestController;
import unravel.erd.RequestEdge;

public class TimeLineToERDConverter {

	private static final long THRESHOLD = 1;

	private ErdBuilder builder;
//	private Map<String, Actor> actors;
	private Map<String, BoundaryObject> boundaryObjects;
//	private Map<String, Controller> controllers;
	private Map<String, Entity> entities;

	public List<EnrichedRobustnessDiagram> convertTimeline(TimeLine timeline) {

		List<EnrichedRobustnessDiagram> erds = new ArrayList<EnrichedRobustnessDiagram>();
		int index = 0;
		for (SessionTimeLine sessionTL : timeline.getSessions()) {
			_init();
			erds.add(_convertSessionTimeline(sessionTL));
			System.out.println("ERD #" + index + " created");
			index++;
		}
		return erds;
	}

	private void _init() {
		builder = new ErdBuilder();
//		actors = new HashMap<>();
		boundaryObjects = new HashMap<>();
//		controllers = new HashMap<>();
		entities = new HashMap<>();
	}

	private EnrichedRobustnessDiagram _convertSessionTimeline(SessionTimeLine sessionTL) {
		Actor actor = builder.addActor("actor");

		// TESTME divide the timesteps in groups belonging to the same request
		List<List<TimeStep>> timeGroups = getTimeGroups(sessionTL);
		ListIterator<List<TimeStep>> currentTimeGroup = timeGroups.listIterator();

		BoundaryObject previousPage = null;
		List<RequestController> previousNavigation = null;

		while (currentTimeGroup.hasNext()) {
			boolean firstTimeGroup = !currentTimeGroup.hasPrevious();
			ListIterator<TimeStep> currentTimeStep = currentTimeGroup.next().listIterator();

			List<RequestController> navigation = new ArrayList<>();

			while (currentTimeStep.hasNext()) {
				boolean firstTimeStepOfGroup = !currentTimeStep.hasPrevious();
				TimeStep timeStep = currentTimeStep.next();
				BoundaryObject nextPage = getBoundaryObject(actor, timeStep.getPageId());

				// Connect with previous page
				if (firstTimeStepOfGroup) {
					if (firstTimeGroup == false) { // No previous TimeGroup to link
						List<RequestEdge> candidateNavigations = getNavigations(previousPage, nextPage);

						if (isNewNavigation(previousNavigation, candidateNavigations)) {
							String label = _getNavigationLabel(previousPage, nextPage, candidateNavigations.size());
							builder.addRequestEdge(previousPage, nextPage, previousNavigation, label);
						}
					}

					previousPage = nextPage;
					previousNavigation = navigation;
				}

				RequestController requestController = builder.addRequestController();
				navigation.add(requestController);

				ListIterator<InstanceMethod> currentMethod = timeStep.getMethodCalls().listIterator();
				while (currentMethod.hasNext()) {
					InstanceMethod method = currentMethod.next();
					Entity entity = getEntity(method);
					Controller controller = getController(entity, method);
					CallController parent = getParentController(method, requestController); // FIXME

					if (parent != null) {
						builder.addCallEdge(parent, controller);
					} else {
						// FIXME Manage the case of a root ContextController
						if (controller instanceof CallController) {
							requestController.addCallController((CallController) controller);
						}
					}
				}

				if (firstTimeStepOfGroup) {
					previousPage = nextPage;
				}
			}
		}

		return builder.buildErd();
	}

	private boolean isNewNavigation(List<RequestController> navigation, List<RequestEdge> candidates) {
		for (RequestEdge candidate : candidates) {
			if (navigation.equals(candidate.getRequestControllers())) {
				return false;
			}
		}
		return true;
	}

	private List<RequestEdge> getNavigations(BoundaryObject previousPage, BoundaryObject nextPage) {
		List<RequestEdge> navigations = new ArrayList<>();
		for (RequestEdge requestEdge : previousPage.getOuterRequestEdges()) {
			if (requestEdge.getLandingBoundaryObject() == nextPage) {
				navigations.add(requestEdge);
			}
		}
		return navigations;
	}

	private CallController getParentController(InstanceMethod method, RequestController requestController) {
		List<CallController> callControllers = requestController.getCallControllers();
		ListIterator<String> currentMethod = method.getCallHierarchy().listIterator();

		Controller candidateParent = null;

		while (currentMethod.hasNext()) {

			if (currentMethod.hasPrevious() == false) { // First time
				if (callControllers.size() == 0) { return null; }
				candidateParent = callControllers.get(callControllers.size() - 1);
			} else { // Second time onward
				if (candidateParent instanceof CallController == false) { return null; }
				CallController callCtr = (CallController) candidateParent;
				candidateParent = callCtr.getCallEdges().get(callCtr.getCallEdges().size() - 1).getLandingController();
			}

			String parentMethod = currentMethod.next();
			if (parentMethod.contains(getMethod(candidateParent)) == false) { return null; }
		}
		return (CallController) candidateParent;
	}

	private String getMethod(Controller ctr) {
		if (ctr instanceof CallController) {
			CallController callCtr = (CallController) ctr;
			return callCtr.getEntity().getName() + "." + callCtr.getMethod();
		} else if (ctr instanceof ContextController) {
			ContextController ctxCtr = (ContextController) ctr;
			return ctxCtr.getAction().toString().toLowerCase();
		}
		return null;
	}

	private Entity getEntity(InstanceMethod method) {
		// FIXME The ContextualInstance for context-switch methods is null
		String className = null;
		Scope scope = null;
		if (method.getDeclaringClassName().equals("javax.enterprise.context.Conversation")) {
			className = "javax.enterprise.context.Conversation";
			scope = Scope.CONVERSATION;
		} else {
			className = method.getCallerInstance().getClassName();
			scope = method.getCallerInstance().getScope();
		}

		if (entities.containsKey(className) == false) {
			entities.put(className, builder.addEntity(className, scope));
		}
		return entities.get(className);
	}

	private BoundaryObject getBoundaryObject(Actor actor, String pageId) {
		// Clean pageId
		String pageName = pageId
				.replace("http://", "")
				.replace("https://", "")
				.replace("localhost:8080/", "");

		if (boundaryObjects.containsKey(pageName) == false) {
			BoundaryObject boundaryObject = builder.addBoundaryObject(pageName);
			builder.addViewEdge(actor, boundaryObject);
			boundaryObjects.put(pageName, boundaryObject);
		}
		return boundaryObjects.get(pageName);
	}

	private Controller getController(Entity entity, InstanceMethod method) {
		if (entity.getName() == "javax.enterprise.context.Conversation")
			switch(method.getMethodName()) {
				case "begin":
					return builder.addContextController(Scope.CONVERSATION, Action.BEGIN);
				case "end":
					return builder.addContextController(Scope.CONVERSATION, Action.END);
			}

		return builder.addCallController(
				entity,
				method.getMethodName(),
				method.getParametersStr());
	}

	private List<List<TimeStep>> getTimeGroups(SessionTimeLine sessionTimeLine) {
		List<TimeStep> timeSteps = sessionTimeLine.getTimeSteps();

		List<List<TimeStep>> timeGroups = new ArrayList<List<TimeStep>>();
		List<TimeStep> timeGroup = new ArrayList<TimeStep>();
		timeGroups.add(timeGroup);

		for (int index = 0; index < timeSteps.size(); index++) {
			timeGroup.add(timeSteps.get(index));
			if (index < timeSteps.size() - 1 && // Has next
					areConcatenated(timeGroup.get(0), timeSteps.get(index + 1)) == false) {
				timeGroup = new ArrayList<TimeStep>();
				timeGroups.add(timeGroup);
			}
		}
		return timeGroups;
	}

	private static String _getNavigationLabel(BoundaryObject startingPage, BoundaryObject landingPage, int index) {
		return "nav " + landingPage.getName() + " " + index;
	}

	private boolean areConcatenated(TimeStep firstStep, TimeStep secondStep) {
		long first = firstStep.getBeginTime().toNanoOfDay();
		long second = secondStep.getBeginTime().toNanoOfDay();
		long difference = second - first;
		long threshold = LocalTime.ofSecondOfDay(THRESHOLD).toNanoOfDay();

		return secondStep.getBeginTime().toNanoOfDay() -
				firstStep.getBeginTime().toNanoOfDay() <
				LocalTime.ofSecondOfDay(THRESHOLD).toNanoOfDay();
	}

}
