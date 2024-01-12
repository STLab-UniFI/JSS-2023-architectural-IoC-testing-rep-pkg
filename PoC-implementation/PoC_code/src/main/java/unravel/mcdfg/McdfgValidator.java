package unravel.mcdfg;

public class McdfgValidator {

	/*
	 * Default constructor
	 */
	public McdfgValidator() {}
	
	public boolean validate(Node n) {
		if (n == null) {
			return false;
		}
		return true;
	}
	
	public boolean validate(Edge e) {
		if (e == null) {
			return false;
		}
		if (e.getStartingNode() == null) {
			return false;
		}
		if (e.getLandingNode() == null) {
			return false;
		}
		return true;
	}
	
	public boolean validate(ManagedComponent mc) {
		if (mc == null) {
			return false;
		}
		return true;
	}
	
	public boolean validate(ManagedComponent mc, String method, String[] arguments) {
		if (validate(mc) == false) {
			return false;
		}
		if (method == null || method == "") {
			return false;
		}
		for (String arg : arguments) {
			if (arg == null || arg == "") {
				return false;
			}
		}
		return true;
	}
	
	public boolean validate(Entry e) {
		if (e == null) {
			return false;
		}
		return true;
	}
}
