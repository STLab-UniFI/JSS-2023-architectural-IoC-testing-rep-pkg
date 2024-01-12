package timeLine;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SessionTimeLine {

	private String sessionId;
	private List<TimeStep> timeSteps;
	private List<ContextualInstance> currentlyLivingInstances;

	public SessionTimeLine(String sessionId) {
		this.sessionId = sessionId;
		timeSteps = new ArrayList<TimeStep>();
		currentlyLivingInstances = new ArrayList<ContextualInstance>();
	}

	public void decorateCurrentStepWithRequestInfo(List<Object> objects, List<InstanceMethod> observedMethods) {
		decorateLastStepWithLivingInstancesInfo(objects);
		decoratedLastStepWithMethodCalls(observedMethods);
	}

	private void decorateLastStepWithLivingInstancesInfo(List<Object> retrievedContextualInstances) {
		List<ContextualInstance> newLivingInstances = getNewLivingInstancesFromObjList(retrievedContextualInstances);
		currentlyLivingInstances.addAll(newLivingInstances);
		TimeStep currentTimeStep = getLastTimeStep();
		currentTimeStep.setLivingInstances(currentlyLivingInstances);
		currentTimeStep.setGeneratedInstances(newLivingInstances);
	}

	private List<ContextualInstance> getNewLivingInstancesFromObjList(List<Object> candidates) {
		List<ContextualInstance> newCtxInstances = new ArrayList<ContextualInstance>();
		for (Object retrievedObj : candidates) {
			boolean alreadyLiving = false;
			for (ContextualInstance livingCtxInstance : currentlyLivingInstances) {
				if (livingCtxInstance.isTheSameInstance(retrievedObj)) {
					alreadyLiving = true;
				}
			}
//			System.out.println("Created Instances: ");
			if (!alreadyLiving) {
//				System.out.println("instance: " + retrievedObj);
				ContextualInstance newCtxInstance = new ContextualInstance(retrievedObj, sessionId);
				newCtxInstances.add(newCtxInstance);
			}
		}
		return newCtxInstances;
	}

	private void decoratedLastStepWithMethodCalls(List<InstanceMethod> observedMethods) {
		for(InstanceMethod method : observedMethods) {
			ContextualInstance callerInstance = retrieveInstanceFromName(method.getDeclaringClassName());
			method.setCallerInstance(callerInstance);
			TimeStep lastTimeStep = getLastTimeStep();
			lastTimeStep.addMethodCall(method);
		}
	}

	private ContextualInstance retrieveInstanceFromName(String declaringClassName) {
		for(ContextualInstance instance : currentlyLivingInstances) {
			if(instance.getClassName().equals(declaringClassName))
				return instance;
		}
		return null;
	}

	public void decorateCurrentStepWithDestroyedInstances(List<Object> retrievedContextualInstances) {
		List<ContextualInstance> destroyedInstances = getDeadInstancesfromObjList(retrievedContextualInstances);
		currentlyLivingInstances.removeAll(destroyedInstances);

		TimeStep currentTimeStep = getLastTimeStep();
		currentTimeStep.setDestroyedInstances(destroyedInstances);
	}
	
	
	public void decorateCurrentStepWithTimeStamps(LocalTime startRequestTime, LocalTime closeRequestTime) {
		TimeStep currentTimeStep = getLastTimeStep();
		currentTimeStep.setBeginTime(startRequestTime);
		currentTimeStep.setEndTime(closeRequestTime);
	}
	
	public void close() {
		TimeStep currentTimeStep = getLastTimeStep();
		currentTimeStep.setDestroyedInstances(currentlyLivingInstances);
		currentlyLivingInstances.clear();
	}

	private List<ContextualInstance> getDeadInstancesfromObjList(List<Object> candidates) {

		ArrayList<ContextualInstance> deathCtxInstance = new ArrayList<ContextualInstance>();

		for (ContextualInstance livingCtxInstance : currentlyLivingInstances) {
			boolean retrieved = false;
			for (Object retrievedInstance : candidates) {
				if (livingCtxInstance.isTheSameInstance(retrievedInstance)) {
					retrieved = true;
				}
			}
//			System.out.println("Dead Instances: ");
			if (!retrieved) {
//				System.out.println("istance: " + livingCtxInstance.getInstance());
				deathCtxInstance.add(livingCtxInstance);
			}
		}
		return deathCtxInstance;
	}

	private TimeStep getLastTimeStep() {
		return this.getTimeSteps().get(this.getTimeSteps().size() - 1);
	}
	
	

	// getters and setters
	public String getSessionId() {
		return sessionId;
	}

	public void recordNewTimeStep(TimeStep timeStep) {
		this.getTimeSteps().add(timeStep);
	}

	public List<TimeStep> getTimeSteps() {
		return timeSteps;
	}

}
