package unravel.mcdfg;

/**
 * 
 */
public class Request extends Context {

	/**
	 * Main constructor
	 */
	public Request() {
		super();
	}
	
	/**
	 * Copy constructor
	 */
	private Request(Request r) {
		super(r);
	}
    
	// methods

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void close() {}

	@Override
	public Request getCopy() {
		return new Request(this);
	}
}