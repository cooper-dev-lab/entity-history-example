package com.example.entityhistoryexample.lazy.repository;

import com.example.entityhistoryexample.lazy.domain.AccountHistoryV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepositoryV3 extends JpaRepository<AccountHistoryV3, Long> {
}
