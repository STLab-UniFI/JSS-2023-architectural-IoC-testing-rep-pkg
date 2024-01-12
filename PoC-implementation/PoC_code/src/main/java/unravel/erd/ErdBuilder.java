package unravel.erd;

import java.util.List;

import unravel.cdi.Action;
import unravel.cdi.Scope;

/**
 * Builder for ERDs
 */
public class ErdBuilder {

	static public class Settings {

		public boolean dependencyEdgesAsDependencies = false;

		public Settings() {
			this(null);
		}

		public Settings(Settings s) {
			if (s != null) {
				dependencyEdgesAsDependencies = s.dependencyEdgesAsDependencies;
			}
		}
	}

	// attributes

	private EnrichedRobustnessDiagram erd;
	private ErdValidator v;
	private Settings settings;

	/**
	 * Default constructor
	 */
	public ErdBuilder() {
		erd = new EnrichedRobustnessDiagram();
		v = new ErdValidator();
		settings = new Settings();
	}

	/**
	 * Main constructor
	 */
	public ErdBuilder(Settings s) {
		erd = new EnrichedRobustnessDiagram();
		v = new ErdValidator();
		settings = new Settings(s);
	}

	// methods

	public EnrichedRobustnessDiagram buildErd() {
		return erd;
	}

    public void eraseErd() {
    	erd = new EnrichedRobustnessDiagram();
    }

	public Actor addActor(String name) {
		Actor a = new Actor(name);
		erd.addActor(a);
		return a;
	}

	public BoundaryObject addBoundaryObject(String name) {
		BoundaryObject bo = new BoundaryObject(name);
		erd.addBoundaryObject(bo);
		return bo;
	}

	public RequestController addRequestController() {
		RequestController ctr = new RequestController();
		erd.addController(ctr);
		return ctr;
	}

	public CallController addCallController(Entity e, String method,
			String[] arguments) {
		return _addCallController(e, method, arguments);
	}

	public CallController addCallController(Entity e, String method) {
		return _addCallController(e, method, new String[] {});
	}

	private CallController _addCallController(Entity e, String method,
			String[] arguments) {
		if (v.validate(e)) {
			CallController ctr = new CallController(e, method, arguments);
			erd.addController(ctr);
			return ctr;
		}
		else {
			return null;
		}
	}

	public ContextController addContextController(Scope s, Action ca) {
		ContextController ctr = new ContextController(s, ca);
		erd.addController(ctr);
		return ctr;
	}

	public Entity addEntity(String name, Scope s) {
		return addEntity(name, s, false);
	}

	public Entity addEntity(String name, Scope s, boolean isPreexisting) {
		Entity e = new Entity(name, s);
		erd.addEntity(e);

		if (isPreexisting) {
			erd.addPreexisting(e);
		}
		return e;
	}

	public ViewEdge addViewEdge(Actor a, BoundaryObject bo) {
		if (v.validate(a) && erd.contains(a) && v.validate(bo) && erd.contains(bo)) {
			ViewEdge ve = new ViewEdge(a, bo);
			a.addViewEdge(ve);
			bo.addViewEdge(ve);
			erd.addViewEdge(ve);
			return ve;
		}
		else {
			return null;
		}
	}

	// XXX Deprecated
	public InitializationEdge addInitializationEdge(BoundaryObject bo,
			CallController callCtr) {
		RequestController requestCtr = addRequestController();
		requestCtr.addCallController(callCtr);
		return addInitializationEdge(bo, List.of(requestCtr));
	}

	public InitializationEdge addInitializationEdge(BoundaryObject bo,
			List<RequestController> rqstCtrs) {
		if (v.validate(bo) && erd.contains(bo) && v.validate(rqstCtrs) &&
				erd.contains(rqstCtrs)) {
			InitializationEdge ie = new InitializationEdge(bo, rqstCtrs);
			bo.addInitializationEdge(ie);
			erd.addInitializationEdge(ie);
			return ie;
		}
		else {
			return null;
		}
	}

	// XXX Deprecated
	public RequestEdge addRequestEdge(BoundaryObject startingBo,
			BoundaryObject landingBo, CallController callCtr, String label) {
		RequestController requestCtr = addRequestController();
		requestCtr.addCallController(callCtr);
		return addRequestEdge(startingBo, landingBo, List.of(requestCtr), label);
	}

	public RequestEdge addRequestEdge(BoundaryObject startingBo,
			BoundaryObject landingBo, List<RequestController> requestCtrs, String label) {
		if (v.validate(startingBo) && erd.contains(startingBo) && v.validate(requestCtrs) &&
				erd.contains(requestCtrs) && v.validate(landingBo) &&
				erd.contains(landingBo)) {
			RequestEdge re = new RequestEdge(startingBo, requestCtrs, landingBo, label);
			startingBo.addOuterRequestEdge(re);
			landingBo.addInnerRequestEdge(re);
			erd.addRequestEdge(re);
			return re;
		}
		else {
			return null;
		}
	}

	public CallEdge addCallEdge(CallController parentCtr, Controller childCtr) {
		return addCallEdge(parentCtr, childCtr, parentCtr.getCallEdges().size());
	}

	public CallEdge addCallEdge(CallController parentCtr, Controller childCtr,
			int index) {
		if (v.validate(parentCtr) && erd.contains(parentCtr) && v.validate(childCtr) &&
				 erd.contains(childCtr) && v.validateIndex(parentCtr, index) &&
				 (childCtr instanceof RequestController) == false) { // FIXME
			CallEdge ce = new CallEdge(parentCtr, childCtr);
			parentCtr.addCallEdge(ce, index);
			if (childCtr instanceof CallController) {
				CallController parentCallCtr = (CallController) parentCtr;
				CallController childCallCtr = (CallController) childCtr;
				parentCallCtr.getEntity().addDependency(childCallCtr.getEntity());
			}
			erd.addCallEdge(ce);
			return ce;
		}
		return null;
	}

	public DependencyEdge addDependencyEdge(CallController callCtr, Entity e) {
		if (v.validate(callCtr) && erd.contains(callCtr) && v.validate(e) &&
				erd.contains(e)) {
			DependencyEdge de = new DependencyEdge(callCtr, e);
			callCtr.addDependencyEdge(de);
			e.addDependencyEdge(de);
			if (settings.dependencyEdgesAsDependencies) {
				callCtr.getEntity().addDependency(e);
			}
			erd.addDependencyEdge(de);
			return de;
		}
		else {
			return null;
		}
	}

	/*public Actor getDefaultActor() {
		return erd.getDefaultActor();
	}*/
}