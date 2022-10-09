package com.example.entityhistoryexample.hibernate_listener.repository;

import com.example.entityhistoryexample.hibernate_listener.domain.AccountHistoryV4;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepositoryV4 extends JpaRepository<AccountHistoryV4, Long> {
}
