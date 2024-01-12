package unravel.mcdfg;

import unravel.cdi.Scope;
import unravel.cdi.Action;

/**
 *
 */
public class ContextBoundaryEdge extends Edge {

	// attributes

	final Scope scope;
	final Action action;

	/**
	 * Main constructor
	 */
	public ContextBoundaryEdge(Node startingN, Node landingN, Scope s,
			Action ca) {
		super(startingN, landingN);
		scope = s;
		action = ca;
	}

	/**
	 * Copy constructor
	 */
	private ContextBoundaryEdge(ContextBoundaryEdge cbe) {
		super(cbe);
		scope = cbe.getScope();
		action = cbe.getAction();
	}

	// methods

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((action == null) ? 0 : action.hashCode());
//		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
//		return result;
//	}

	public boolean isIdenticalTo(Edge e) {
		if (this == e) {
			return true;
		}
		if (e == null) {
			return false;
		}
		if (getClass() != e.getClass()) {
			return false;
		}
		ContextBoundaryEdge other = (ContextBoundaryEdge) e;
		if (action != other.action) {
			return false;
		}
		if (scope != other.scope) {
			return false;
		}
		return true;
	}

	@Override
	public ContextBoundaryEdge getCopy() {
		return new ContextBoundaryEdge(this);
	}

	@Override
	public String getLabel() {
		return "cb " + action.toString().toLowerCase() + " " +
				scope.toString().toLowerCase();
	}

	public Scope getScope() {
		return scope;
	}

	public Action getAction() {
		return action;
	}
}