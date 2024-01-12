package timeLine;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeStep {

	private String sessionId;
	private int requestId;
	private List<ContextualInstance> livingInstances;
	private List<ContextualInstance> generatedInstances;
	private List<ContextualInstance> destroyedInstances;
	private List<InstanceMethod> methodCalls;
	private LocalTime beginTime;
	private LocalTime endTime;
	private String pageId;

	public TimeStep(int requestId, String sessionId, String pageId) {
		this.requestId = requestId;
		this.sessionId = sessionId;
		this.pageId = pageId;
		this.livingInstances = new ArrayList<ContextualInstance>();
		this.generatedInstances = new ArrayList<ContextualInstance>();
		this.destroyedInstances = new ArrayList<ContextualInstance>();
		this.methodCalls = new ArrayList<InstanceMethod>();
	}

	public List<ContextualInstance> getLivingInstances() {
		return livingInstances;
	}

	public void setLivingInstances(List<ContextualInstance> instances) {
		// defensive copy
		this.livingInstances = new ArrayList<ContextualInstance>(instances);
	}

	public String getSessionId() {
		return sessionId;
	}

	public List<ContextualInstance> getGeneratedInstances() {
		return generatedInstances;
	}

	public void setGeneratedInstances(List<ContextualInstance> generatedInstances) {
		this.generatedInstances = generatedInstances;
	}

	public List<ContextualInstance> getDestroyedInstances() {
		return destroyedInstances;
	}

	public void setDestroyedInstances(List<ContextualInstance> destroyedInstances) {
		this.destroyedInstances = destroyedInstances;
	}

	public int getRequestId() {
		return requestId;
	}

	public void addMethodCall(InstanceMethod method) {
		getMethodCalls().add(method);
	}

	public List<InstanceMethod> getMethodCalls() {
		return methodCalls;
	}

	public LocalTime getBeginTime() {
		return beginTime;
		
	}

	public void setBeginTime(LocalTime beginTime) {
		this.beginTime = beginTime;
		
	}

	public LocalTime getEndTime() {
		return endTime;
		
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
		
	}

	public String getPageId() {
		return pageId;
	}

}
