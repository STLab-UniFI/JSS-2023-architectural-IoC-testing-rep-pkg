package unravel.erd;

import java.util.List;
import java.util.ArrayList;

/**
 * Simplified UML communication/collaboration diagram
 */
public class EnrichedRobustnessDiagram {

	// attributes

	private List<Actor> actors;
	private List<BoundaryObject> boundaryObjects;
	private List<Controller> controllers;
	private List<Entity> entities;
	private List<Entity> preexisting;

	private List<ViewEdge> viewEdges;
	private List<InitializationEdge> initializationEdges;
	private List<RequestEdge> requestEdges;
	private List<CallEdge> callEdges;
	private List<DependencyEdge> dependencyEdges;

	/**
	 * Default constructor
	 */
	public EnrichedRobustnessDiagram() {
		actors = new ArrayList<Actor>();
		boundaryObjects = new ArrayList<BoundaryObject>();
		controllers = new ArrayList<Controller>();
		entities = new ArrayList<Entity>();
		preexisting = new ArrayList<Entity>();

		viewEdges = new ArrayList<ViewEdge>();
		initializationEdges = new ArrayList<InitializationEdge>();
		requestEdges = new ArrayList<RequestEdge>();
		callEdges = new ArrayList<CallEdge>();
		dependencyEdges = new ArrayList<DependencyEdge>();
	}

	// methods

	public boolean contains(Actor a) {
		if (actors.contains(a)) {
			return true;
		}
		return false;
	}

	public boolean contains(BoundaryObject bo) {
		if (boundaryObjects.contains(bo)) {
			return true;
		}
		return false;
	}

	public boolean contains(Controller ctr) {
		if (controllers.contains(ctr)) {
			return true;
		}
		return false;
	}

	public boolean contains(List<RequestController> ctrs) {
		for (Controller ctr : ctrs) {
			if (controllers.contains(ctr) == false) {
				return false;
			}
		}
		return true;
	}

	public boolean contains(Entity e) {
		if (entities.contains(e)) {
			return true;
		}
		return false;
	}

	public Actor getDefaultActor() {
		if (actors.size() > 0) {
			return actors.get(0);
		} else {
			return null;
		}
	}

	// TODO: return unmodifiable views of components

	public List<Actor> getActors() {
		return actors;
	}

	public List<BoundaryObject> getBoundaryObjects() {
		return boundaryObjects;
	}

	public List<Controller> getControllers() {
		return controllers;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public List<Entity> getPreexisting() {
		return preexisting;
	}

	public List<ViewEdge> getViewEdges() {
		return viewEdges;
	}

	public List<InitializationEdge> getInitializationEdges() {
		return initializationEdges;
	}

	public List<RequestEdge> getRequestEdges() {
		return requestEdges;
	}

	public List<CallEdge> getCallEdges() {
		return callEdges;
	}

	public List<DependencyEdge> getDependencyEdges() {
		return dependencyEdges;
	}

	public void addActor(Actor a) {
		actors.add(a);
	}

	public void addBoundaryObject(BoundaryObject bo) {
		boundaryObjects.add(bo);
	}

	public void addController(Controller ctr) {
		controllers.add(ctr);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void addPreexisting(Entity e) {
		preexisting.add(e);
	}

	public void addViewEdge(ViewEdge ve) {
		viewEdges.add(ve);
	}

	public void addInitializationEdge(InitializationEdge ie) {
		initializationEdges.add(ie);
	}

	public void addRequestEdge(RequestEdge re) {
		requestEdges.add(re);
	}

	public void addCallEdge(CallEdge ce) {
		callEdges.add(ce);
	}

	public void addDependencyEdge(DependencyEdge de) {
		dependencyEdges.add(de);
	}
}