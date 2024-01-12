package unravel.mcdfg;

/**
 * 
 */
public class Session extends Context {

	// attributes
	
	private Request request;
	private Conversation conversation;
    
	/**
	 * Default constructor
	 */
	public Session() {
		super();
	}
	
	/**
	 * Copy constructor
	 */
	private Session(Session s) {
		super(s);
		if (s.getRequest() != null) {
			request = s.getRequest().getCopy();
		}
		if (s.getConversation() != null) {
			conversation = s.getConversation().getCopy();
		}
	}
    
	// methods
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((conversation == null) ? 0 : conversation.hashCode());
		result = prime * result + ((request == null) ? 0 : request.hashCode());
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
		Session other = (Session) obj;
		if (conversation == null) {
			if (other.conversation != null) {
				return false;
			}
		} else if (!conversation.equals(other.conversation)) {
			return false;
		}
		if (request == null) {
			if (other.request != null) {
				return false;
			}
		} else if (!request.equals(other.request)) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void close() {
		closeRequest();
		closeConversation();
	}

	@Override
	public Session getCopy() {
		return new Session(this);
	}

	public boolean openRequest() {
		if (request == null) {
			request = new Request();
			return true;
		}
		return false;
	}
	
	public boolean openConversation() {
		return openConversation(Conversation.Type.LONG_RUNNING);
	}
	
	public boolean openConversation(Conversation.Type t) {
		if (conversation == null) {
			conversation = new Conversation(t);
			return true;
		}
		else {
			return conversation.openConversation(t);
		}
	}
	
	public boolean closeRequest() {
		if (request != null) {
			request.close();
			request = null;
			return true;
		}
		return false;
	}
	
	public boolean closeConversation() {
		if (conversation != null) {
			if (conversation.closeConversation() == false) {
				conversation.close();
				conversation = null;
			}
			return true;
		}
		return false;
	}
	
	public Request getRequest() {
		return request;
	}
	
	public Conversation getConversation() {
		if (conversation != null) {
			Conversation c = conversation.getConversation();
			if (c != null) {
				return c;
			}
		}
		return conversation;
	}
}