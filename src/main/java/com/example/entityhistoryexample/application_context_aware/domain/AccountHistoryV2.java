package com.example.entityhistoryexample.application_context_aware.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountHistoryV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", updatable = false)
    private String accountNumber;

    @Column(name = "current_balance")
    private long currentBalance;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    private AccountHistoryV2(String accountNumber, long currentBalance, LocalDateTime createdTime) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.createdTime = createdTime;
    }

    public static AccountHistoryV2 create (String accountNumber, long currentBalance, LocalDateTime createdTime) {
        return new AccountHistoryV2(accountNumber, currentBalance, createdTime);
    }

}
