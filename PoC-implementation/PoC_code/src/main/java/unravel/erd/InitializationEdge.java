package unravel.erd;

import java.util.List;
import java.util.UUID;

import unravel.misc.Identifiable;

/**
 *
 */
public class InitializationEdge implements Identifiable {

	// attributes

	final private String uuid;
    private BoundaryObject boundaryObject;
    private List<RequestController> requestControllers;

    /**
     * Main constructor
     */
    public InitializationEdge(BoundaryObject bo, List<RequestController> rqstCtrs) {
    	uuid = UUID.randomUUID().toString();
    	boundaryObject = bo;
    	requestControllers = rqstCtrs;
    }

	@Override
	public String getUuid() {
		return uuid;
	}

	public BoundaryObject getBoundaryObject() {
		return boundaryObject;
	}

	public List<RequestController> getRequestControllers() {
		return requestControllers;
	}
}