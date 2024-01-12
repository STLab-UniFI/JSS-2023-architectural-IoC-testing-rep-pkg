package beanTimelineManager.conversationManager;

import static java.util.stream.Collectors.toSet;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InterceptionFactory;

import beanTimelineManager.methodsManager.MethodCallsInterceptorBindingLiteral;

@Alternative
@Priority(500)
@ApplicationScoped
public class ConversationProducer {

	@Produces
	public Conversation produce(InterceptionFactory<Conversation> interceptionFactory, BeanManager beanManager) {

		Conversation convBean = null;

		convBean = createRef(beanManager.resolve(beanManager.getBeans(Conversation.class).stream()
		      .filter(e -> !e.getBeanClass().equals(ConversationProducer.class))
				.collect(toSet())), beanManager);

		interceptionFactory.configure().add(new MethodCallsInterceptorBindingLiteral());

		return interceptionFactory.createInterceptedInstance(new ConversationWrapper(convBean));
	}

	private Conversation createRef(Bean<?> bean, BeanManager beanManager) {
		return (Conversation) beanManager.getReference(bean, Conversation.class,
				beanManager.createCreationalContext(bean));
	}
}
