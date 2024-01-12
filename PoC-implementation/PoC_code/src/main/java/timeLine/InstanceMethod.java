package timeLine;

import java.util.Arrays;
import java.util.List;

import javax.interceptor.InvocationContext;

public class InstanceMethod {

	private String declaringClassName;
	private String methodName;
	private ContextualInstance callerInstance;
	private Object[] parameters;
	private List<String> callHierarchy;

	public InstanceMethod(String declaringClassName, String methodName) {
		this.declaringClassName = declaringClassName;
		this.methodName = methodName;
	}

	public InstanceMethod(InvocationContext ctx) {
		this.declaringClassName = ctx.getMethod().getDeclaringClass().getName();
		this.methodName = ctx.getMethod().getName();
		this.parameters = ctx.getParameters();
	}

	public String getDeclaringClassName() {
		return declaringClassName;
	}

	public String getMethodName() {
		return methodName;
	}

	public ContextualInstance getCallerInstance() {
		return callerInstance;
	}

	public void setCallerInstance(ContextualInstance callerInstance) {
		this.callerInstance = callerInstance;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public String[] getParametersStr() {
		return Arrays.stream(parameters).map(Object::toString).toArray(String[]::new);
	}

	public List<String> getCallHierarchy() {
		return callHierarchy;
	}

	public void setCallHierarchy(List<String> callHierarchy) {
		this.callHierarchy = callHierarchy;
	}

}
