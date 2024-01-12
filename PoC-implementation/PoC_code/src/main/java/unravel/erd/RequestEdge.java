package unravel.erd;

import java.util.List;
import java.util.UUID;

import unravel.misc.Identifiable;

/**
 *
 */
public class RequestEdge implements Identifiable {

	// attributes

	final private String uuid;
	final private String label;
    private BoundaryObject startingBoundaryObject;
    private List<RequestController> requestControllers;
    private BoundaryObject landingBoundaryObject;

    /**
     * Main constructor
     */
    public RequestEdge(BoundaryObject startingBo, List<RequestController> rqstCtrs,
    		BoundaryObject landingBo, String label) {
    	uuid = UUID.randomUUID().toString();
    	this.label = label;
    	startingBoundaryObject = startingBo;
    	requestControllers = rqstCtrs;
    	landingBoundaryObject = landingBo;
    }

	@Override
	public String getUuid() {
		return uuid;
	}

	public String getLabel() {
		return label;
	}

	public BoundaryObject getStartingBoundaryObject() {
		return startingBoundaryObject;
	}

	public List<RequestController> getRequestControllers() {
		return requestControllers;
	}

	public BoundaryObject getLandingBoundaryObject() {
		return landingBoundaryObject;
	}
}