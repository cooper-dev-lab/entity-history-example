package com.example.entityhistoryexample.hibernate_listener.config;

import com.example.entityhistoryexample.hibernate_listener.listener.AccountHistoryListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountHistoryConfig implements InitializingBean {

    private final EntityManagerFactory entityManagerFactory;

    private final AccountHistoryListener accountHistoryListener;

    @Override
    public void afterPropertiesSet() {
        addEventListenerInSessionFactory();
    }

    private void addEventListenerInSessionFactory() {
        SessionFactoryImpl sessionFactoryImpl = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry eventListenerRegistry
                = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        eventListenerRegistry.appendListeners(EventType.POST_INSERT, accountHistoryListener);
    }

}
