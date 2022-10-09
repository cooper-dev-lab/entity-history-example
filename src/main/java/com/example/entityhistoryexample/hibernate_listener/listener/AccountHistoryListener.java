package com.example.entityhistoryexample.hibernate_listener.listener;

import com.example.entityhistoryexample.hibernate_listener.domain.AccountHistoryV4;
import com.example.entityhistoryexample.hibernate_listener.domain.AccountV4;
import com.example.entityhistoryexample.hibernate_listener.repository.AccountHistoryRepositoryV4;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountHistoryListener implements PostInsertEventListener {

    private final AccountHistoryRepositoryV4 accountHistoryRepositoryV4;

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Object entity = event.getEntity();
        boolean accountType = entity instanceof AccountV4;

        if (accountType) {
            saveAccountCreateHistory((AccountV4) entity);
        }

    }

    private void saveAccountCreateHistory(AccountV4 accountV4) {
        AccountHistoryV4 accountHistoryV4 = AccountHistoryV4.create(
                accountV4.getAccountNumber(),
                accountV4.getBalance()
        );

        accountHistoryRepositoryV4.save(accountHistoryV4);
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return true;
    }

}
