package beanTimelineManager.methodsManager;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import beanTimelineManager.filter.InstanceFilter;

public class MethodCallsInterceptorExt implements Extension {

	void processAnnotatedType(@Observes ProcessAnnotatedType<?> event) {
		if (InstanceFilter.instanceShouldBeAnnotated(event)) {
			event.configureAnnotatedType().add(new MethodCallsInterceptorBindingLiteral());
		}
	}

}
