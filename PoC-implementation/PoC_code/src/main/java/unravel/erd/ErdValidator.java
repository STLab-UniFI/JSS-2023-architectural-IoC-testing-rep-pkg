package unravel.erd;

import java.util.List;

/**
 *
 */
public class ErdValidator {

	public boolean validate(Actor a) {
		if (a != null) {
			return true;
		}
		return false;
	}

	public boolean validate(BoundaryObject bo) {
		if (bo != null) {
			return true;
		}
		return false;
	}

	public boolean validate(Controller ctr) {
		if (ctr != null) {
			return true;
		}
		return false;
	}

	public boolean validate(List<RequestController> ctrs) {
		for (Controller ctr : ctrs) {
			if (ctr == null) {
				return false;
			}
		}
		return true;
	}

	public boolean validate(Entity e) {
		if (e != null) {
			return true;
		}
		return false;
	}

	public boolean validateIndex(CallController ctr, int index) {
		if (0 <= index && index <= ctr.getCallEdges().size()) {
			return true;
		}
		return false;
	}
}