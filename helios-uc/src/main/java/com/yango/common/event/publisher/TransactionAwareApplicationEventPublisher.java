package com.yango.common.event.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@Primary
public class TransactionAwareApplicationEventPublisher implements ApplicationEventPublisher {

	private ApplicationEventPublisher eventPublisher;

	private static final Logger logger = LoggerFactory.getLogger(TransactionAwareApplicationEventPublisher.class);

	@Autowired
	public TransactionAwareApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void publishEvent(final ApplicationEvent event) {
		logger.info("Event start");
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					eventPublisher.publishEvent(event);
				}
			});
		} else {
			eventPublisher.publishEvent(event);
		}
		logger.info("Event end");
	}

	@Override
	public void publishEvent(final Object event) {
		logger.info("Event start");
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					eventPublisher.publishEvent(event);
				}
			});
		} else {
			eventPublisher.publishEvent(event);
		}
		logger.info("Event end");
	}

}
