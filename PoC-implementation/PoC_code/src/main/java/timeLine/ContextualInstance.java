package timeLine;

import java.lang.annotation.Annotation;

import unravel.cdi.Scope;

public class ContextualInstance {

	private Object instance;
	private String sessionID;
	private String className;
	private Scope scope;

	public ContextualInstance(Object instance, String sessionID) {
		this.setInstance(instance);
		this.sessionID = sessionID;
		this.className = getInstanceClass().getName();
		_initScope(getInstanceClass());
	}

	public boolean isTheSameInstance(Object instanceToCompare) {
		return this.instance == instanceToCompare;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getClassName() {
		return className;
	}

	public Scope getScope() {
		return scope;
	}

	private Class<?> getInstanceClass() {
		if (isProxy(instance.getClass()))
			return instance.getClass().getSuperclass();
		else
			return instance.getClass();
	}

	private boolean isProxy(Class<?> cls) {
		if (cls.getSimpleName().contains("Proxy$"))
			return true;
		else
			return false;
	}

	private void _initScope(Class<?> instanceClass) {
		Annotation[] annotations = instanceClass.getAnnotations();

		for (Annotation annotation : annotations) {
			String annotationStr = annotation.toString();
			if (annotationStr.contains("ApplicationScoped")) {
				this.scope = Scope.APPLICATION;
			} else if (annotationStr.contains("SessionScoped")) {
				this.scope = Scope.SESSION;
			} else if (annotationStr.contains("RequestScoped")) {
				this.scope = Scope.REQUEST;
			} else if (annotationStr.contains("ConversationScoped")) {
				this.scope = Scope.CONVERSATION;
			}
		}

		if (scope == null) {
			scope = Scope.DEPENDENT;
		}
	}
}
