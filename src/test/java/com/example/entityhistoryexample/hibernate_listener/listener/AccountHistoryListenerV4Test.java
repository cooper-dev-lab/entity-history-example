package com.example.entityhistoryexample.hibernate_listener.listener;

import com.example.entityhistoryexample.hibernate_listener.domain.AccountHistoryV4;
import com.example.entityhistoryexample.hibernate_listener.domain.AccountV4;
import com.example.entityhistoryexample.hibernate_listener.repository.AccountHistoryRepositoryV4;
import com.example.entityhistoryexample.hibernate_listener.repository.AccountRepositoryV4;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountHistoryListenerV4Test {

    @Autowired
    private AccountHistoryRepositoryV4 accountHistoryRepositoryV4;

    @Autowired
    private AccountRepositoryV4 accountRepositoryV4;

    @DisplayName("계좌 히스토리 저장 확인")
    @Test
    void saveAccountHistoryWhenAccountSaved() {
        //given
        AccountV4 accountV4 = AccountV4.create("123-123-123", "12345", 1234);

        //when
        accountRepositoryV4.save(accountV4);

        //then
        List<AccountHistoryV4> accountHistories = accountHistoryRepositoryV4.findAll();
        assertThat(accountHistories).hasSize(1);

        AccountHistoryV4 accountHistoryV4 = accountHistories.get(0);
        assertThat(accountHistoryV4.getAccountNumber()).isEqualTo("123-123-123");
        assertThat(accountHistoryV4.getCurrentBalance()).isEqualTo(1234);
    }

}