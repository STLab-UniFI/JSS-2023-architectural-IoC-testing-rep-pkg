package beanTimelineManager.filter;

import java.util.function.Predicate;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class InstanceFilter {
	
	private static final String PACKAGE_OF_INTEREST = "toyApp";

	public static boolean instanceShouldBeAnnotated(ProcessAnnotatedType<?> event) {
		return event.getAnnotatedType().getBaseType().toString().contains(PACKAGE_OF_INTEREST)
				&& !event.getAnnotatedType().getBaseType().toString().contains("beanTimeLineManager")
				|| event.getAnnotatedType().getBaseType().toString().contains("javax.enterprise.context.Conversation");
	}
	
	public static Predicate<Bean<?>> getInstancesToObserveFilter() {
		return bean -> bean.getBeanClass().toString().contains(PACKAGE_OF_INTEREST); 
	}

}
