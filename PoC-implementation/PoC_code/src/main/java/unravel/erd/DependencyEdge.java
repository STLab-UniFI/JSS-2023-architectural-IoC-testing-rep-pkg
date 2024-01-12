package unravel.erd;

import java.util.UUID;

import unravel.misc.Identifiable;

/**
 * 
 */
public class DependencyEdge implements Identifiable {

	// attributes
	
	final private String uuid;
    private Controller controller;
    private Entity entity;
	
    /**
     * Main constructor
     */
    public DependencyEdge(Controller ctr, Entity e) {
    	uuid = UUID.randomUUID().toString();
    	controller = ctr;
    	entity = e;
    }
    
	@Override
	public String getUuid() {
		return uuid;
	}

	public Controller getController() {
		return controller;
	}

	public Entity getEntity() {
		return entity;
	}
}