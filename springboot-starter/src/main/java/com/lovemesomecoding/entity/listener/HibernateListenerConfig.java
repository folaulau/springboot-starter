package com.lovemesomecoding.entity.listener;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateListenerConfig {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private HibernateListener userAuditListener;

	@PostConstruct
	public void registerListeners() {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);

        EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry()
				.getService(EventListenerRegistry.class);

        registry.getEventListenerGroup(EventType.POST_INSERT).appendListeners(userAuditListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListeners(userAuditListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListeners(userAuditListener);
	}

}
