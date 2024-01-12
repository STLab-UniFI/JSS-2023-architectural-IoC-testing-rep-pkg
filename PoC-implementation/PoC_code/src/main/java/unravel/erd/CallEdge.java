package unravel.erd;

import java.util.UUID;

import unravel.misc.Identifiable;

/**
 * 
 */
public class CallEdge implements Identifiable {

	// attributes
	
	final private String uuid;
	private int index;
    private Controller startingController;
    private Controller landingController;
	
    /**
     * Main constructor
     */
    public CallEdge(Controller startingCtr, Controller landingCtr) {
    	uuid = UUID.randomUUID().toString();
    	this.index = -1;
    	startingController = startingCtr;
    	landingController = landingCtr;
    }
    
	@Override
	public String getUuid() {
		return uuid;
	}

	public int getIndex() {
		return index;
	}

	public Controller getStartingController() {
		return startingController;
	}

	public Controller getLandingController() {
		return landingController;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
}