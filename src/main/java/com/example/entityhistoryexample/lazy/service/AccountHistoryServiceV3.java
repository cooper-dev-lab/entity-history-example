package com.example.entityhistoryexample.lazy.service;

import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import com.example.entityhistoryexample.lazy.domain.AccountHistoryV3;
import com.example.entityhistoryexample.lazy.repository.AccountHistoryRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountHistoryServiceV3 {

    private final AccountHistoryRepositoryV3 accountHistoryRepositoryV3;

    public void addAccountHistory(AccountHistoryRequestDto accountHistoryRequestDto) {
        AccountHistoryV3 accountHistoryV2 = AccountHistoryV3.create(
                accountHistoryRequestDto.getAccountNumber(),
                accountHistoryRequestDto.getBalance(),
                accountHistoryRequestDto.getCreatedTime()
        );

        accountHistoryRepositoryV3.save(accountHistoryV2);

        log.debug("accountHistory added: {}", accountHistoryV2);
    }

}
