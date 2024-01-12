package beanTimelineManager.methodsManager;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import timeLine.InstanceMethod;

@MethodCallsInterceptorBinding
@Interceptor
@Dependent
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class MethodCallsInterceptor implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

//	private List<Class<?>> involvedClasses;
	private List<Method> involvedMethods;

	@Inject
	MethodsCollector methodsCollector;

	public void resetInteractions() {
//		involvedClasses = new ArrayList<Class<?>>();
		involvedMethods = new ArrayList<Method>();
	}

	@AroundInvoke
	public Object manageMethodCall(InvocationContext ctx) throws Exception {

		InstanceMethod method = new InstanceMethod(ctx);

		methodsCollector.addObservedMethod(method);
		method.setCallHierarchy(filter(Thread.currentThread().getStackTrace()));

		try {
			Object result = ctx.proceed();
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

	private List<String> filter(StackTraceElement[] stack) {
		List<String> involvedMethods = methodsCollector.getObservedMethods().stream()
				.map(element -> element.getDeclaringClassName() + "." + element.getMethodName())
				.collect(Collectors.toList());
		List<String> callHierarchy = List.of(stack).stream()
				.filter(element -> element.getLineNumber() != -1) // Filter out proxy methods
				.map(element -> element.getClassName() + "." + element.getMethodName())
				.collect(Collectors.toList());
		callHierarchy.removeIf(element -> involvedMethods.contains(element) == false);
		Collections.sort(callHierarchy, Collections.reverseOrder());
		return callHierarchy;
	}

//	public List<Class<?>> getInvolvedClasses() {
//		return involvedClasses;
//	}

	public List<Method> getInvolvedMethods() {
		return involvedMethods;
	}

}
