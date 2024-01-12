package unravel.erd;

import java.util.Objects;

import unravel.cdi.Action;
import unravel.cdi.Scope;

/**
 *
 */
public class ContextController extends Controller {

	// attributes

    private final Scope scope;
    private final Action action;

	/**
	 * Main constructor
	 */
	public ContextController(Scope scope, Action action) {
		super();
		this.scope = scope;
		this.action = action;
	}

	// methods

	public Scope getScope() {
		return scope;
	}

	public Action getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, scope);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ContextController))
			return false;
		ContextController other = (ContextController) obj;
		return action == other.action && scope == other.scope;
	}
}