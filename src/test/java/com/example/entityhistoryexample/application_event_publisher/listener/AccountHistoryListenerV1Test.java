package com.example.entityhistoryexample.application_event_publisher.listener;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountHistoryV1;
import com.example.entityhistoryexample.application_event_publisher.domain.AccountV1;
import com.example.entityhistoryexample.application_event_publisher.repository.AccountHistoryRepositoryV1;
import com.example.entityhistoryexample.application_event_publisher.repository.AccountRepositoryV1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountHistoryListenerV1Test {

    @Autowired
    private AccountHistoryRepositoryV1 accountHistoryRepositoryV1;

    @Autowired
    private AccountRepositoryV1 accountRepositoryV1;

    @DisplayName("계좌 히스토리 저장 확인")
    @Test
    void saveAccountHistoryWhenAccountSaved() {
        //given
        AccountV1 accountV1 = AccountV1.create("123-123-123", "12345", 1234);

        //when
        accountRepositoryV1.save(accountV1);

        //then
        List<AccountHistoryV1> accountHistories = accountHistoryRepositoryV1.findAll();
        assertThat(accountHistories).hasSize(1);

        AccountHistoryV1 accountHistoryV1 = accountHistories.get(0);
        assertThat(accountHistoryV1.getAccountNumber()).isEqualTo("123-123-123");
        assertThat(accountHistoryV1.getCurrentBalance()).isEqualTo(1234);
    }


}