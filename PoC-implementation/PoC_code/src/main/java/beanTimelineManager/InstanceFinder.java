package beanTimelineManager;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;

public class InstanceFinder {

//	static final String MANAGED_BEAN_CLASSNAME = "class org.jboss.weld.bean.ManagedBean";

	private BeanManager beanManager;

	private Predicate<Bean<?>> customBeanFilter;

	private boolean shouldApplyCustomFiltering;

	public InstanceFinder(BeanManager beanManager) {
		this.beanManager = beanManager;
		this.setShouldApplyCustomFiltering(false);
	}

	public InstanceFinder(BeanManager beanManager, Predicate<Bean<?>> filter) {
		this.beanManager = beanManager;
		this.customBeanFilter = filter;
		this.setShouldApplyCustomFiltering(true);
	}

	public List<Object> retrieveContextualInstances() {
		Set<Bean<?>> beanSet = retrieveAllBeans();
		if (isShouldApplyCustomFiltering())
			beanSet = applyCustomFiltering(beanSet);
		return ConvertBeansIntoContextualInstances(beanSet);
	}

	private Set<Bean<?>> retrieveAllBeans() {
		Set<Bean<?>> beanSet = new HashSet<>(beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {
			private static final long serialVersionUID = 1L;
		}));
		return beanSet;
	}

	private Set<Bean<?>> applyCustomFiltering(Set<Bean<?>> beanSet) {
		return beanSet.stream().filter(getCustomBeanFilter()).collect(Collectors.toSet());
	}

	private List<Object> ConvertBeansIntoContextualInstances(Set<Bean<?>> beanSet) {
		List<Object> contextualInstances = new ArrayList<Object>();
		for (Bean<?> bean : beanSet) {
			Set<Bean<?>> sameClassBeanSet = extractSameClassBeans(beanSet, bean);
			beanSet = removeSameClassBeans(beanSet, bean);
			Bean<? extends Object> resolvolvedBean = beanManager.resolve(sameClassBeanSet);
			Object ctxInstance = getContextualInstanceFromBean(resolvolvedBean);
			if (ctxInstance != null)
				contextualInstances.add(ctxInstance);
		}
		return contextualInstances;
	}

	private Set<Bean<?>> extractSameClassBeans(Set<Bean<?>> beanSet, Bean<?> currentBean) {
		return beanSet.stream().filter(bean -> bean.getBeanClass() == currentBean.getBeanClass()
				&& bean.getClass() == currentBean.getClass()) // XXX aggiunto perché lanciava eccezione al resolve
				// trovava sia la classe che gli interessava sia quella relativa al producer.
				// ci sono alcuni casi in cui i bean hanno la stessa bean class ma non la stessa class (alcuni producer method alcuni no)
				.collect(Collectors.toSet());
	}

	private Set<Bean<?>> removeSameClassBeans(Set<Bean<?>> beanSet, Bean<?> currentBean) {
		return beanSet.stream().filter(bean -> bean.getBeanClass() != currentBean.getBeanClass()
				|| bean.getClass() != currentBean.getClass()) // XXX anche questo stato aggiunto per il motivo di extractsameclass
				.collect(Collectors.toSet());
	}

//	private Object getContextualInstanceFromBean(Bean<?> bean) {
//		Class<? extends Annotation> scope = bean.getScope();
//		return beanManager.getContext(scope).get(bean);
//	}

	private Object getContextualInstanceFromBean(Bean<?> bean) {
		Class<? extends Annotation> scope = bean.getScope();
		if (scope.getName().contains("ViewScoped")) {
			try {
				return null;
			} catch (NullPointerException npe) {
				System.out.println("## NullPointerException catturata!");
				return null;
			}
		}
		else {
			// default behavior
			return beanManager.getContext(scope).get(bean);
		}
	}

	public Predicate<Bean<?>> getCustomBeanFilter() {
		return customBeanFilter;
	}

	public void setCustomBeanFilter(Predicate<Bean<?>> customBeanFilter) {
		this.customBeanFilter = customBeanFilter;
	}

	public boolean isShouldApplyCustomFiltering() {
		return shouldApplyCustomFiltering;
	}

	public void setShouldApplyCustomFiltering(boolean shouldApplyCustomFiltering) {
		this.shouldApplyCustomFiltering = shouldApplyCustomFiltering;
	}
}