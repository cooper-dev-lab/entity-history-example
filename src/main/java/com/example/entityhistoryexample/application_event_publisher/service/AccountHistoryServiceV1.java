package com.example.entityhistoryexample.application_event_publisher.service;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountHistoryV1;
import com.example.entityhistoryexample.application_event_publisher.repository.AccountHistoryRepositoryV1;
import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountHistoryServiceV1 {

    private final AccountHistoryRepositoryV1 accountHistoryRepositoryV1;

    @EventListener
    public void addAccountHistory(AccountHistoryRequestDto accountHistoryRequestDto) {
        AccountHistoryV1 accountHistoryV1 = AccountHistoryV1.create(
                accountHistoryRequestDto.getAccountNumber(),
                accountHistoryRequestDto.getBalance(),
                accountHistoryRequestDto.getCreatedTime()
        );

        accountHistoryRepositoryV1.save(accountHistoryV1);

        log.debug("accountHistory added: {}", accountHistoryV1);
    }

}
