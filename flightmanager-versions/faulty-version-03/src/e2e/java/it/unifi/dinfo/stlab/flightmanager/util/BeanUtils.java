package it.unifi.dinfo.stlab.flightmanager.util;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.spi.AlterableContext;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;

public class BeanUtils {

	static final String packageName = "it.unifi.ing.swam.";
	static final String managedBeanClassName = "class org.jboss.weld.bean.ManagedBean";

	private BeanManager beanManager;

	public BeanUtils(BeanManager bm) {
		this.beanManager = bm;
	}

	public void cleanContexts() {
		List<Bean<?>> beans = retrieveImplementedBeans();
		for (Bean<?> bean : beans) {
			Class<? extends Annotation> scope = bean.getScope();
			AlterableContext context = (AlterableContext) beanManager.getContext(scope);
			destroybean(context, bean.getBeanClass());
		}
	}

	private void destroybean(AlterableContext context, Class beanClass) {
				for (Bean<?> bean : beanManager.getBeans(beanClass)) {
					Object instance = context.get(bean);
					System.out.println("Istanza di: " + bean);
					if (instance != null)
						context.destroy(bean);
				}
	}

	private List<Bean<?>> retrieveImplementedBeans() {
		Set<Bean<?>> beanSet = retrieveAllBeans();
		return selectOnlyImplementedBeans(beanSet);
	}

	private Set<Bean<?>> retrieveAllBeans() {
		Set<Bean<?>> beanSet = beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {
			private static final long serialVersionUID = 1L;
		});
		return beanSet;
	}

	private List<Bean<?>> selectOnlyImplementedBeans(Set<Bean<?>> beanSet) {
		List<Bean<?>> beanList = beanSet.stream()
				.filter(bean -> bean.getClass().toString().contains(managedBeanClassName)
						&& bean.getBeanClass().toString().contains(packageName))
				.collect(Collectors.toList());
		return beanList;
	}
}
