package unravel.erd;

import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import unravel.cdi.Scope;
import unravel.misc.Identifiable;

/**
 *
 */
public class Entity implements Identifiable {

	// attributes

	private final String uuid;
	private final String name;
	private final Scope scope;
	private Map<Entity, Integer> dependencies;
	private List<DependencyEdge> dependencyEdges;

	/**
	 * Main constructor
	 */
	public Entity(String name, Scope scope) {
		uuid = UUID.randomUUID().toString();
		this.name = name;
		this.scope = scope;
		dependencies = new LinkedHashMap<Entity, Integer>();
		dependencyEdges = new ArrayList<DependencyEdge>();
	}

    // methods

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

	public Map<Entity, Integer> getDependencies() {
		return dependencies;
	}

	public List<DependencyEdge> getDependencyEdges() {
		return dependencyEdges;
	}

	public void addDependency(Entity dependency) {
		if (dependencies.containsKey(dependency)) {
			Integer redundancies = dependencies.get(dependency);
			dependencies.replace(dependency, ++redundancies);
		} else {
			dependencies.put(dependency, Integer.valueOf(0));
		}
	}

	public void addDependencyEdge(DependencyEdge de) {
		dependencyEdges.add(de);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, scope);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Entity))
			return false;
		Entity other = (Entity) obj;
		return Objects.equals(name, other.name) && scope == other.scope;
	}
}