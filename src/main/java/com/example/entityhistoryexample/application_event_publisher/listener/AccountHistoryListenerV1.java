package com.example.entityhistoryexample.application_event_publisher.listener;

import com.example.entityhistoryexample.application_event_publisher.domain.AccountV1;
import com.example.entityhistoryexample.dto.AccountHistoryRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

public class AccountHistoryListenerV1 {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostPersist
    public void postPersist(AccountV1 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        // event 에 대해 후처리하는 함수 : callback
        // 일의 순서(X)
        // WebFlux 를 통해 더욱 간편하게 할 요소가 있다. (행위를 넘길 수 있다 / 일급 객체(자바는 클래스가 가장 최소단위, 코틀린은 함수를 최소단위를 할 수 있다))
        // rollback의 발생할 경우에 대해서 고민할 필요가 있다.
        applicationEventPublisher.publishEvent(accountHistoryRequestDto);
        // back pressure : publisher - subscriber 의 대응을 보조해주는 역할 (e.g. 유실 역할)
        //  - RabbitMQ, kafka
        //      - 비즈니스 로직이 더욱 어려울 때 적용하는 것이 좋을 것 같다.
        //      - 장점 : 안정성, 가용성
        //      - 장점 : 구현의 복잡도,
    }

    @PostUpdate
    public void postUpdate(AccountV1 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        applicationEventPublisher.publishEvent(accountHistoryRequestDto);
    }
}
