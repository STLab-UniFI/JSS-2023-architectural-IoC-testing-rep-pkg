package unravel.mcdfg;

import java.util.UUID;
import unravel.cdi.Scope;
import unravel.misc.Identifiable;

/**
 *
 */
public class ManagedComponent implements Identifiable {

	// attributes

	final private String uuid;
	final private String name;
	final private Scope scope;

	/**
	 * Main constructor
	 */
	public ManagedComponent(String name, Scope s) {
		uuid = UUID.randomUUID().toString();
		this.name = name;
		scope = s;
	}

	// methods

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ManagedComponent other = (ManagedComponent) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (scope != other.scope) {
			return false;
		}
		return true;
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public Scope getScope() {
		return scope;
	}
}