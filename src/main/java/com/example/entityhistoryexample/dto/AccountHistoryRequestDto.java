package com.example.entityhistoryexample.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountHistoryRequestDto {

    private final String accountNumber;

    private final long balance;

    private final LocalDateTime createdTime;

    public static AccountHistoryRequestDto create(String accountNumber, long balance, LocalDateTime createdTime) {
        return new AccountHistoryRequestDto(accountNumber, balance, createdTime);
    }

}
