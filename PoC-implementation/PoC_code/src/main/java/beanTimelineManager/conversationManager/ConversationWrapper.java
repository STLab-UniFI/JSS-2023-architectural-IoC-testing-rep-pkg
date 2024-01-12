package beanTimelineManager.conversationManager;

import javax.enterprise.context.Conversation;

public class ConversationWrapper implements Conversation {
	
	private Conversation conversation;
	
	public ConversationWrapper(Conversation conversation) {
		this.conversation = conversation;
	}

	@Override
	public void begin() {
		this.conversation.begin();
	}

	@Override
	public void begin(String id) {
		this.conversation.begin(id);
	}

	@Override
	public void end() {
		this.conversation.end();
	}

	@Override
	public String getId() {
		return this.conversation.getId();
	}

	@Override
	public long getTimeout() {
		return this.conversation.getTimeout();
	}

	@Override
	public void setTimeout(long milliseconds) {
		this.conversation.setTimeout(milliseconds);
	}

	@Override
	public boolean isTransient() {
		return this.conversation.isTransient();
	}

}
