package com.example.entityhistoryexample.lazy.repository;

import com.example.entityhistoryexample.lazy.domain.AccountV3;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepositoryV3 extends JpaRepository<AccountV3, Long> {
}
