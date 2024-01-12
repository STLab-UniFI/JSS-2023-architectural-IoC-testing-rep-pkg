package unravel.erd;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import unravel.misc.Identifiable;

/**
 * 
 */
public class BoundaryObject implements Identifiable {

	// attributes

	final private String uuid;
	final private String name;
	private List<ViewEdge> viewEdges;
	private List<InitializationEdge> initializationEdges;
	private List<RequestEdge> innerRequestEdges;
	private List<RequestEdge> outerRequestEdges;
	
	/**
	 * Main constructor
	 */
	public BoundaryObject(String name) {
		uuid = UUID.randomUUID().toString();
		this.name = name;
		viewEdges = new ArrayList<ViewEdge>();
		initializationEdges = new ArrayList<InitializationEdge>();
		innerRequestEdges = new ArrayList<RequestEdge>();
		outerRequestEdges = new ArrayList<RequestEdge>();
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

	public List<InitializationEdge> getInitializationEdges() {
		return initializationEdges;
	}

	public List<RequestEdge> getInnerRequestEdges() {
		return innerRequestEdges;
	}

	public List<RequestEdge> getOuterRequestEdges() {
		return outerRequestEdges;
	}
	
	public void addViewEdge(ViewEdge ve) {
		viewEdges.add(ve);
	}
	
	public void addInitializationEdge(InitializationEdge ie) {
		initializationEdges.add(ie);
	}
	
	public void addInnerRequestEdge(RequestEdge re) {
		innerRequestEdges.add(re);
	}
	
	public void addOuterRequestEdge(RequestEdge re) {
		outerRequestEdges.add(re);
	}
}