package unravel.erd;

import java.util.UUID;

import unravel.misc.Identifiable;

/**
 * 
 */
public class ViewEdge implements Identifiable {

	// attributes
	
	private final String uuid;
	Actor actor;
	BoundaryObject boundaryObject;
	
	/**
	 * Main constructor
	 */
	public ViewEdge(Actor actor, BoundaryObject boundaryObject) {
		uuid = UUID.randomUUID().toString();
		this.actor = actor;
		this.boundaryObject = boundaryObject;
	}
	
	// methods
	
	@Override
	public String getUuid() {
		return uuid;
	}
	
	public Actor getActor() {
		return actor;
	}

	public BoundaryObject getBoundaryObject() {
		return boundaryObject;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public void setBoundaryObject(BoundaryObject boundaryObject) {
		this.boundaryObject = boundaryObject;
	}

}