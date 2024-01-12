package unravel.mcdfg;

/**
 * 
 */
public class Conversation extends Context {

	// enumerators
	
	enum Type {
		TRANSIENT,
		LONG_RUNNING
	}
	
	// attributes
	
	private Type type;
	private Conversation conversation;
    
	/**
	 * Default constructor
	 */
	public Conversation() {
		this(Type.LONG_RUNNING);
	}
    
	/**
	 * Main constructor
	 */
	public Conversation(Type t) {
		super();
		type = t;
	}
	
	/**
	 * Copy constructor
	 */
	private Conversation(Conversation c) {
		super(c);
		type = c.getType();
		if (c.getConversation() != null) {
			conversation = c.getConversation().getCopy();
		}
	}
    
	// methods
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((conversation == null) ? 0 : conversation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Conversation other = (Conversation) obj;
		if (conversation == null) {
			if (other.conversation != null) {
				return false;
			}
		} else if (!conversation.equals(other.conversation)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	@Override
	protected void close() {
		closeConversation();
	}
	
	@Override
	public Conversation getCopy() {
		return new Conversation(this);
	}
	
	public boolean openConversation() {
		return openConversation(Type.LONG_RUNNING);
	}
	
	public boolean openConversation(Type t) {
		if (conversation == null) {
			conversation = new Conversation(t);
			return true;
		}
		else {
			return conversation.openConversation(t);
		}
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
	
	public Conversation getConversation() {
		if (conversation != null) {
			Conversation c = conversation.getConversation();
			if (c != null) {
				return c;
			}
		}
		return conversation;
	}

	public Type getType() {
		return type;
	}

	public boolean isTransient() {
		if (type == Type.TRANSIENT) {
			return true;
		}
		return false;
	}

	public void setType(Type type) {
		this.type = type;
	}
}