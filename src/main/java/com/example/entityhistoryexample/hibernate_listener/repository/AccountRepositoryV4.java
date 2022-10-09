package com.example.entityhistoryexample.hibernate_listener.repository;

import com.example.entityhistoryexample.hibernate_listener.domain.AccountV4;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositoryV4 extends JpaRepository<AccountV4, Long> {
}
