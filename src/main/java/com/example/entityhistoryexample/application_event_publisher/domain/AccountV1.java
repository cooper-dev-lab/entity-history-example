package com.example.entityhistoryexample.application_event_publisher.domain;

import com.example.entityhistoryexample.application_event_publisher.listener.AccountHistoryListenerV1;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = AccountHistoryListenerV1.class)
public class AccountV1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", updatable = false)
    private String accountNumber;

    private String password;

    private long balance;

    private AccountV1(String accountNumber, String password, long balance) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
    }

    public static AccountV1 create(String accountNumber, String password, long balance) {
        return new AccountV1(accountNumber, password, balance);
    }

}
