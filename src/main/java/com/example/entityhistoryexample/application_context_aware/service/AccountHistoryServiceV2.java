package com.example.entityhistoryexample.application_context_aware.service;

import com.example.entityhistoryexample.application_context_aware.domain.AccountHistoryV2;
import com.example.entityhistoryexample.application_context_aware.repository.AccountHistoryRepositoryV2;
import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountHistoryServiceV2 {

    private final AccountHistoryRepositoryV2 accountHistoryRepositoryV2;

    public void addAccountHistory(AccountHistoryRequestDto accountHistoryRequestDto) {
        AccountHistoryV2 accountHistoryV2 = AccountHistoryV2.create(
                accountHistoryRequestDto.getAccountNumber(),
                accountHistoryRequestDto.getBalance(),
                accountHistoryRequestDto.getCreatedTime()
        );

        accountHistoryRepositoryV2.save(accountHistoryV2);

        log.debug("accountHistory added: {}", accountHistoryV2);
    }

}
