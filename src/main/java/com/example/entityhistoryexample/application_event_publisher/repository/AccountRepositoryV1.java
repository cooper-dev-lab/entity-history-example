package com.example.entityhistoryexample.application_event_publisher.repository;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountV1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositoryV1 extends JpaRepository<AccountV1, Long> {
}
