package unravel.mcdfg;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 */
public abstract class Context {

	// attributes
	
	private List<ManagedComponent> definedComponents;
    
	/**
	 * Main constructor
	 */
	public Context() {
		definedComponents = new ArrayList<ManagedComponent>();
	}
	
	protected Context(Context c) {
		definedComponents = new ArrayList<ManagedComponent>(c.getDefinedComponents());
	}
    
	// methods
	
	abstract protected void close();
	abstract public Context getCopy();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definedComponents == null) ? 0 :
				definedComponents.hashCode());
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
		Context other = (Context) obj;
		if (definedComponents == null) {
			if (other.definedComponents != null) {
				return false;
			}
		} else if (!definedComponents.equals(other.definedComponents)) {
			return false;
		}
		return true;
	}
	
	public ManagedComponent getManagedComponent(String name) {
		for (ManagedComponent mc : definedComponents) {
			if (mc.getName().equals(name)) {
				return mc;
			}
		}
		return null;
	}
	
	public List<ManagedComponent> getDefinedComponents() {
		return definedComponents;
	}

	public void addDefinedComponent(ManagedComponent mc) {
		definedComponents.add(mc);
	}

}