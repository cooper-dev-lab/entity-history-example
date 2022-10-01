package com.example.entityhistoryexample.application_event_publisher.repository;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountHistoryV1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepositoryV1 extends JpaRepository<AccountHistoryV1, Long> {
}
