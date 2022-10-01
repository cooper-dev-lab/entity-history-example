package com.example.entityhistoryexample.application_context_aware.repository;

import com.example.entityhistoryexample.application_context_aware.domain.AccountHistoryV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepositoryV2 extends JpaRepository<AccountHistoryV2, Long> {
}
