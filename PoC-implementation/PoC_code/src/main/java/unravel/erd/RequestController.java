package unravel.erd;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

/**
 *
 */
public class RequestController extends Controller {

    // attributes

	protected List<CallController> callControllers;

	/**
	 * Default constructor
	 */
	public RequestController() {
		super();
		callControllers = new ArrayList<CallController>();
	}

	// methods

	public List<CallController> getCallControllers() {
		return callControllers;
	}

	public void addCallController(CallController callCtr) {
		callControllers.add(callCtr);
	}

	public void addCallController(CallController callCtr, int index) {
		callControllers.add(index, callCtr);
	}

	@Override
	public int hashCode() {
		return Objects.hash(callControllers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RequestController))
			return false;
		RequestController other = (RequestController) obj;
		return callControllers.equals(other.callControllers);
	}
}