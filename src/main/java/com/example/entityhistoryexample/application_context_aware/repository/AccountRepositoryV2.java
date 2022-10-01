package com.example.entityhistoryexample.application_context_aware.repository;

import com.example.entityhistoryexample.application_context_aware.domain.AccountV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositoryV2 extends JpaRepository<AccountV2, Long> {
}
