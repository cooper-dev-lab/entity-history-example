package com.example.entityhistoryexample.application_event_publisher.listener;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountV1;
import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

@Component
public class AccountHistoryListenerV1 {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostPersist
    public void postPersist(AccountV1 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        applicationEventPublisher.publishEvent(accountHistoryRequestDto);
    }

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
