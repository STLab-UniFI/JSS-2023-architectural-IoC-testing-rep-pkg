package unravel.mcdfg;

import java.util.Arrays;

/**
 *
 */
public class NavigationEdge extends Edge {

	// attributes

	final private ManagedComponent managedComponent;
	final private String method;
	final private String[] arguments;

	/**
	 * Main constructor
	 */
	public NavigationEdge(Node startingN, Node landingN, ManagedComponent mc,
			String method, String[] arguments) {
		super(startingN, landingN);
		managedComponent = mc;
		this.method = method;
		this.arguments = arguments;
	}

	/**
	 * Copy constructor
	 */
	private NavigationEdge(NavigationEdge ne) {
		super(ne);
		managedComponent = ne.getManagedComponent();
		method = ne.getMethod();
		arguments = ne.getArguments().clone();
	}

	// methods

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + Arrays.hashCode(arguments);
//		result = prime * result + ((managedComponent == null) ? 0 : managedComponent.hashCode());
//		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		NavigationEdge other = (NavigationEdge) e;
		if (!Arrays.equals(arguments, other.arguments)) {
			return false;
		}
		if (managedComponent == null) {
			if (other.managedComponent != null) {
				return false;
			}
		} else if (!managedComponent.equals(other.managedComponent)) {
			return false;
		}
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		} else if (!method.equals(other.method)) {
			return false;
		}
		return true;
	}

	@Override
	public NavigationEdge getCopy() {
		return new NavigationEdge(this);
	}

	@Override
	public String getLabel() {
		return "nav " + managedComponent.getName() + "::" + method + "(" +
				String.join(", ", arguments) + ")";
	}

	public ManagedComponent getManagedComponent() {
		return managedComponent;
	}

	public String getMethod() {
		return method;
	}

	public String[] getArguments() {
		return arguments;
	}
}