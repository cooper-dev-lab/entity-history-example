package com.example.entityhistoryexample.application_context_aware.listener;

import com.example.entityhistoryexample.application_context_aware.domain.AccountHistoryV2;
import com.example.entityhistoryexample.application_context_aware.domain.AccountV2;
import com.example.entityhistoryexample.application_context_aware.repository.AccountHistoryRepositoryV2;
import com.example.entityhistoryexample.application_context_aware.repository.AccountRepositoryV2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AccountHistoryListenerV2Test {

    @Autowired
    private AccountHistoryRepositoryV2 accountHistoryRepositoryV2;

    @Autowired
    private AccountRepositoryV2 accountRepositoryV2;

    @DisplayName("계좌 히스토리 저장 확인")
    @Test
    void saveAccountHistoryWhenAccountSaved() {
        //given
        AccountV2 accountV2 = AccountV2.create("123-123-123", "1234", 1234);

        //when
        accountRepositoryV2.save(accountV2);

        //then
        List<AccountHistoryV2> accountHistories = accountHistoryRepositoryV2.findAll();
        Assertions.assertThat(accountHistories).hasSize(1);

        AccountHistoryV2 accountHistoryV2 = accountHistories.get(0);
        Assertions.assertThat(accountHistoryV2.getAccountNumber()).isEqualTo("123-123-123");
        Assertions.assertThat(accountHistoryV2.getCurrentBalance()).isEqualTo(1234);

    }


}