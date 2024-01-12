package unravel.mcdfg;

import unravel.cdi.Scope;

/**
 *
 */
public class Status {

	// attributes

	private Application application;

	/**
	 * Default constructor
	 */
	public Status() {}

	/**
	 * Copy constructor
	 */
	private Status(Status s) {
		if (s.getApplication() != null) {
			application = s.getApplication().getCopy();
		}
	}

	// methods

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Status other = (Status) obj;
		if (application == null) {
			if (other.application != null) {
				return false;
			}
		} else if (!application.equals(other.application)) {
			return false;
		}
		return true;
	}

	public Status getCopy() {
		return new Status(this);
	}

	public boolean beginApplication() {
		if (application == null) {
			application = new Application();
			return true;
		}
		return false;
	}

	public boolean endApplication() {
		if (application != null) {
			application.close();
			application = null;
			return true;
		}
		return false;
	}

	public boolean beginSession() {
		if (application != null) {
			return application.openSession();
		}
		return false;
	}

	public boolean endSession() {
		if (application != null) {
			return application.closeSession();
		}
		return false;
	}

	public boolean beginRequest() {
		Session s = getSession();
		if (s != null) {
			return s.openRequest();
		}
		return false;
	}

	public boolean endRequest() {
		Session s = getSession();
		if (s != null) {
			return s.closeRequest();
		}
		return false;
	}

	public boolean beginConversation() {
		return beginConversation(Conversation.Type.LONG_RUNNING);
	}

	public boolean beginConversation(Conversation.Type t) {
		Session s = getSession();
		if (s != null) {
			return s.openConversation(t);
		}
		return false;
	}

	public boolean endConversation() {
		Session s = getSession();
		if (s != null) {
			return s.closeConversation();
		}
		return false;
	}

	public Context getContext(Scope s) {
		switch (s) {
		case APPLICATION:
			return getApplication();
		case SESSION:
			return getSession();
		case REQUEST:
			return getRequest();
		case CONVERSATION:
			return getConversation();
		default:
			return null;
		}
	}

	public Application getApplication() {
		return application;
	}

	public Session getSession() {
		if (application != null) {
			return application.getSession();
		}
		return null;
	}

	public Request getRequest() {
		Session s = getSession();
		if (s != null) {
			return s.getRequest();
		}
		return null;
	}

	public Conversation getConversation() {
		Session s = getSession();
		if (s != null) {
			return s.getConversation();
		}
		return null;
	}
}