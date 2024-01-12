package unravel.mcdfg;

/**
 * 
 */
public class Entry {

	// enumerators
	
	public enum Type {
		DEF,
		USE,
		DEF_USE
	}
	
	// attributes
	
	final private Type type;
	final private ManagedComponent managedComponent;
    
	/**
	 * Main constructor
	 */
	public Entry(Type t, ManagedComponent mc) {
		type = t;
		managedComponent = mc;
	}
    
	/**
	 * Copy constructor
	 */
	public Entry(Entry e) {
		type = e.getType();
		managedComponent = e.getManagedComponent();
	}
    
	// methods
	
	/*boolean isEqualTo(Entry e) {
		if (type == e.getType() && managedComponent == e.getManagedComponent()) {
			return true;
		}
		return false;
	}*/
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((managedComponent == null) ? 0 : managedComponent.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Entry other = (Entry) obj;
		if (managedComponent == null) {
			if (other.managedComponent != null) {
				return false;
			}
		} else if (!managedComponent.equals(other.managedComponent)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	Entry getCopy() {
		return new Entry(this);
	}

	public String getLabel() {
		return type.toString() + " " + managedComponent.getName();
	}

	public Type getType() {
		return type;
	}

	public ManagedComponent getManagedComponent() {
		return managedComponent;
	}
}