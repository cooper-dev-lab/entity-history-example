package com.example.entityhistoryexample.lazy.listener;

import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import com.example.entityhistoryexample.lazy.domain.AccountV3;
import com.example.entityhistoryexample.lazy.service.AccountHistoryServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

@Component
public class AccountHistoryListenerV3 {

    @Lazy
    @Autowired
    private AccountHistoryServiceV3 accountHistoryServiceV3;

    @PostPersist
    public void postPersist(AccountV3 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        accountHistoryServiceV3.addAccountHistory(accountHistoryRequestDto);
    }

    @PostUpdate
    public void postUpdate(AccountV3 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        accountHistoryServiceV3.addAccountHistory(accountHistoryRequestDto);
    }
}
