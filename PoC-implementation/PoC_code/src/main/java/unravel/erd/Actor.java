package unravel.erd;

import java.util.UUID;

import unravel.misc.Identifiable;

import java.util.List;
import java.util.ArrayList;

/**
 * Same concept as actors on a UML use case diagram
 */
public class Actor implements Identifiable {

	// attributes

	final private String uuid;
	final private String name;
	private List<ViewEdge> viewEdges;
	
	/**
	 * Main constructor
	 */
	public Actor(String name) {
		uuid = UUID.randomUUID().toString();
		this.name = name;
		viewEdges = new ArrayList<ViewEdge>();
	}
	
	// methods
	
	@Override
	public String getUuid() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}

	public List<ViewEdge> getViewEdges() {
		return viewEdges;
	}
	
	public BoundaryObject getDefaultBoundaryObject() {
		if (viewEdges.size() > 0) {
			return viewEdges.get(0).getBoundaryObject();
		}
		else {
			return null;
		}
	}
	
	public void addViewEdge(ViewEdge ve) {
		viewEdges.add(ve);
	}
}