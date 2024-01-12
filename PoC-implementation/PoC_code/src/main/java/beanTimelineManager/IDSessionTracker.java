package beanTimelineManager;

import javax.enterprise.context.RequestScoped;

@Deprecated
@RequestScoped
public class IDSessionTracker {

	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
