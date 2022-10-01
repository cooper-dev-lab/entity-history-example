package com.example.entityhistoryexample.application_context_aware.listener;

import com.example.entityhistoryexample.application_context_aware.domain.AccountV2;
import com.example.entityhistoryexample.application_context_aware.service.AccountHistoryServiceV2;
import com.example.entityhistoryexample.application_context_aware.utils.BeanUtils;
import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

@Component
public class AccountHistoryListenerV2 {

    @Autowired
    private BeanUtils beanUtils;

    @PostPersist
    public void postPersist(AccountV2 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        AccountHistoryServiceV2 accountHistoryServiceV2 = beanUtils.getBean(AccountHistoryServiceV2.class);
        accountHistoryServiceV2.addAccountHistory(accountHistoryRequestDto);
    }

    @PostUpdate
    public void postUpdate(AccountV2 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        AccountHistoryServiceV2 accountHistoryServiceV2 = beanUtils.getBean(AccountHistoryServiceV2.class);
        accountHistoryServiceV2.addAccountHistory(accountHistoryRequestDto);
    }
}
