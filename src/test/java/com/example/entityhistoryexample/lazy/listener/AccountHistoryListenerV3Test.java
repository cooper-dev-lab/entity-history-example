package com.example.entityhistoryexample.lazy.listener;

import com.example.entityhistoryexample.lazy.domain.AccountHistoryV3;
import com.example.entityhistoryexample.lazy.domain.AccountV3;
import com.example.entityhistoryexample.lazy.repository.AccountHistoryRepositoryV3;
import com.example.entityhistoryexample.lazy.repository.AccountRepositoryV3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountHistoryListenerV3Test {

    @Autowired
    private AccountHistoryRepositoryV3 accountHistoryRepositoryV3;

    @Autowired
    private AccountRepositoryV3 accountRepositoryV3;

    @DisplayName("계좌 히스토리 저장 확인")
    @Test
    void saveAccountHistoryWhenAccountSaved() {
        //given
        AccountV3 accountV3 = AccountV3.create("123-123-123", "12345", 1234);

        //when
        accountRepositoryV3.save(accountV3);

        //then
        List<AccountHistoryV3> accountHistories = accountHistoryRepositoryV3.findAll();
        assertThat(accountHistories).hasSize(1);

        AccountHistoryV3 accountHistoryV3 = accountHistories.get(0);
        assertThat(accountHistoryV3.getAccountNumber()).isEqualTo("123-123-123");
        assertThat(accountHistoryV3.getCurrentBalance()).isEqualTo(1234);
    }

}