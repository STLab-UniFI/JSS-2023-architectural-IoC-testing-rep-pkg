package unravel.mcdfg;

/**
 *
 */
public class SimpleEdge extends Edge {

	/**
	 * Main constructor
	 */
	public SimpleEdge(Node startingN, Node landingN) {
		super(startingN, landingN);
	}

	/**
	 * Copy constructor
	 */
	private SimpleEdge(SimpleEdge se) {
		super(se);
	}

	// methods

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
		return true;
	}

	@Override
	public SimpleEdge getCopy() {
		return new SimpleEdge(this);
	}

	@Override
	public String getLabel() {
		return "";
	}
}