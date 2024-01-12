package timeLine;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TimeLine {

	private List<SessionTimeLine> sessions;

	private int requestCounter;

	private List<TimeStep> timeSteps;


//	@PostConstruct
//	public void init() {
//		sessions = new ArrayList<SessionTimeLine>();
//		requestCounter = 0;
//		timeSteps = new ArrayList<TimeStep>();
//	}

	public TimeLine() {
		sessions = new ArrayList<SessionTimeLine>();
		requestCounter = 0;
		timeSteps = new ArrayList<TimeStep>();
	}

	public void recordNewTimeStep(String sessionId, List<Object> objects, List<InstanceMethod> observedMethods,
			LocalTime startRequestTime, LocalTime closeRequestTime, String pageId) {
		TimeStep timeStep = new TimeStep(requestCounter, sessionId, pageId);
		getTimeSteps().add(timeStep);

		SessionTimeLine session = getSession(sessionId);

		session.recordNewTimeStep(timeStep);
		session.decorateCurrentStepWithRequestInfo(objects, observedMethods);
		session.decorateCurrentStepWithTimeStamps(startRequestTime, closeRequestTime);
		requestCounter++;

	}

	public void concludeCurrentTimeStep(String sessionId, List<Object> initialContextualInstances) {
		SessionTimeLine session = getsessionIfExist(sessionId);
		if(session == null)
			return;
//		System.out.println("SESSION ID: " + sessionId);
		session.decorateCurrentStepWithDestroyedInstances(initialContextualInstances);
	}
	
	public void close() {
		for(SessionTimeLine sessionTL : sessions) {
			sessionTL.close();
		}
	}

	private SessionTimeLine getsessionIfExist(String sessionId) {
		if (sessionId == null)
			return null;
		for (SessionTimeLine session : sessions) {
			if (session.getSessionId().equals(sessionId)) {
				return session;
			}
		}
		return null;
	}

	private SessionTimeLine getSession(String sessionId) {
		for (SessionTimeLine session : getSessions()) {
			if (session.getSessionId().equals(sessionId)) {
				return session;
			}
		}
		return getNewInitializedSession(sessionId);
	}

	private SessionTimeLine getNewInitializedSession(String sessionId) {
		SessionTimeLine newSession = new SessionTimeLine(sessionId);
		getSessions().add(newSession);
		return newSession;
	}
	
	public List<TimeStep> getTimeSteps() {
		return timeSteps;
	}

	public List<SessionTimeLine> getSessions() {
		return sessions;
	}
}
