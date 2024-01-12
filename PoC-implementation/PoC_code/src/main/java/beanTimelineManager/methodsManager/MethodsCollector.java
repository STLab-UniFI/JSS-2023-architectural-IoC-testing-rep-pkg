package beanTimelineManager.methodsManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import timeLine.InstanceMethod;

@ApplicationScoped
public class MethodsCollector {
	
	private List<InstanceMethod> observedMethods;
	
	@PostConstruct
	public void init() {
		observedMethods = new ArrayList<InstanceMethod>();
	}

	public void addObservedMethod(InstanceMethod method) {
		getObservedMethods().add(method);
	}

	public List<InstanceMethod> getObservedMethods() {
		return observedMethods;
	}
	
	public void ClearCollection() {
		observedMethods.clear();
	}

}
