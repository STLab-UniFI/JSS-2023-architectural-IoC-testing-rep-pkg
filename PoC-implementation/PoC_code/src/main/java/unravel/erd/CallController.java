package unravel.erd;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class CallController extends Controller {

    // attributes

	final private Entity entity;
	final private String method;
	final private String[] arguments;
	protected List<CallEdge> callEdges;
	private List<DependencyEdge> dependencyEdges;

	/**
	 * Default constructor
	 */
	public CallController(Entity entity, String method, String[] arguments) {
		super();
		this.entity = entity;
    	this.method = method;
    	if (arguments == null) {
    		arguments = new String[] {};
    	}
    	this.arguments = arguments;
		callEdges = new ArrayList<CallEdge>();
    	dependencyEdges = new ArrayList<DependencyEdge>();
	}

	// methods

	public Entity getEntity() {
		return entity;
	}

	public String getMethod() {
		return method;
	}

	public String[] getArguments() {
		return arguments;
	}

	public List<CallEdge> getCallEdges() {
		return callEdges;
	}

	public void addCallEdge(CallEdge ce) {
		callEdges.add(ce);
		updateIndexes();
	}

	public void addCallEdge(CallEdge ce, int index) {
		callEdges.add(index, ce);
		updateIndexes();
	}

	private void updateIndexes() {
		for (int i = 0; i < callEdges.size(); i++) {
			callEdges.get(i).setIndex(i);
		}
	}

	public List<DependencyEdge> getDependencyEdges() {
		return dependencyEdges;
	}

	public void addDependencyEdge(DependencyEdge de) {
		dependencyEdges.add(de);
	}

	public List<Controller> getChildren() {
		return callEdges.stream().map(edge -> edge.getLandingController()).collect(Collectors.toList());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + Arrays.hashCode(arguments); XXX Input selection
		result = prime * result + Objects.hash(entity, method);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CallController))
			return false;
		CallController other = (CallController) obj;

		return Objects.equals(entity, other.entity) && Objects.equals(method, other.method)
//				&& Arrays.equals(arguments, other.arguments) XXX Input selection
				&& getChildren().equals(other.getChildren());
	}
}