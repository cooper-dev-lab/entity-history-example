package com.example.entityhistoryexample.hibernate_listener.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountV4 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", updatable = false)
    private String accountNumber;

    private String password;

    private long balance;

    private AccountV4(String accountNumber, String password, long balance) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = balance;
    }

    public static AccountV4 create(String accountNumber, String password, long balance) {
        return new AccountV4(accountNumber, password, balance);
    }

}
