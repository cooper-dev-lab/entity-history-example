package com.example.entityhistoryexample.application_event_publisher.listener;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountV1;
import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

public class AccountHistoryListenerV1 {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Async
    @PostPersist
    public void postPersist(AccountV1 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        applicationEventPublisher.publishEvent(accountHistoryRequestDto);
    }

    @Async
    @PostUpdate
    public void postUpdate(AccountV1 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        applicationEventPublisher.publishEvent(accountHistoryRequestDto);
    }
}
