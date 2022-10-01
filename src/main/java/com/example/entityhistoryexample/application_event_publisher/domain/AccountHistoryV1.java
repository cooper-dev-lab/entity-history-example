package com.example.entityhistoryexample.application_event_publisher.domain;

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
public class AccountHistoryV1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", updatable = false)
    private String accountNumber;

    @Column(name = "current_balance")
    private long currentBalance;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    private AccountHistoryV1(String accountNumber, long currentBalance, LocalDateTime createdTime) {
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.createdTime = createdTime;
    }

    public static AccountHistoryV1 create (String accountNumber, long currentBalance, LocalDateTime createdTime) {
        return new AccountHistoryV1(accountNumber, currentBalance, createdTime);
    }

}
