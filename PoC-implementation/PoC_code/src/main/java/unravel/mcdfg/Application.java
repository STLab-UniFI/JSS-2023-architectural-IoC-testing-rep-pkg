package unravel.mcdfg;

/**
 * 
 */
public class Application extends Context {

	// attributes
	
	private Session session;
    
	/**
	 * Default constructor
	 */
	public Application() {
		super();
	}
	
	/**
	 * Copy constructor
	 */
	private Application(Application a) {
		super(a);
		if (a.getSession() != null) {
			session = a.getSession().getCopy();
		}
	}
    
	// methods
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((session == null) ? 0 : session.hashCode());
		return result;
	}

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
		Application other = (Application) obj;
		if (session == null) {
			if (other.session != null) {
				return false;
			}
		} else if (!session.equals(other.session)) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void close() {
		closeSession();
	}

	@Override
	public Application getCopy() {
		return new Application(this);
	}
	
	public boolean openSession() {
		if (session == null) {
			session = new Session();
			return true;
		}
		return false;
	}
	
	public boolean closeSession() {
		if (session != null) {
			session.close();
			session = null;
			return true;
		}
		return false;
	}

	public Session getSession() {
		return session;
	}
}