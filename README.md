# entity-history-example

## 1. EntityListener 이용해 히스토리를 추가하는 방법

1. 해당 프로젝트 예시는 계좌 히스토리를 기록하는 방법에 대해 기록한 내용이다.
2. 계좌 히스토리를 기록하는 상황은 **계좌 생성**, **입출금 시점** 두가지 상황으로 구분할 수 있다.
3. 이를 엔티티 시점으로 접근할 때는 엔티티 생성, 엔티티 수정에 구분된다.
4. 엔티티의 이벤트(e.g. CRUD) 를 후처리하는 콜백함수를 **`EntityListener`** 를 통해 적용할 수 있다.

<br>

## 2. EntityListener 에 히스토리 저장하는 빈 로직을 추가

### (1) BeanUtil 를 통해 빈을 주입하는 방법 (by. ApplicationContextAware)

- **ApplicationContextAware** : ApplicationContextAware interface 를 구현한 빈은 자신을 관리하는 ApplicationContext 인스턴스의 참조를 얻을 수
  있다.
- ApplicationContext 의 참조를 갖는 BeanUtil 을 선언하고 EntityListener 의 필드 주입을 받았다.

**BeanUtils**

```java

@Component
public class BeanUtils implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
```

**AccountHistoryListenerV2**

```java
public class AccountHistoryListenerV2 {

    @Autowired
    private BeanUtils beanUtils;

    @PostPersist
    public void postPersist(AccountV2 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        AccountHistoryServiceV2 accountHistoryServiceV2 = beanUtils.getBean(AccountHistoryServiceV2.class);
        accountHistoryServiceV2.addAccountHistory(accountHistoryRequestDto);
    }
    ...
}

```

<br>

### (2) EntityListener 에 빈을 주입하는 방법

- @Lazy : 빈을 사용할 때 프록시를 초기화하는 어노테이션
- EntityFactoryManager 를 Bean으로 등록할 때 EntityListener 에 대해 Bean을 등록하는
  작업이 존재하는데, EntityListener 에서 EntityManagerFactory 를 사용하는 Bean 이 존재하면 문제가 발생한다. 
- EntityManagerFactory 를 사용하는 Repository component 가 존재할 경우, EntityManagerFactory 를 생성하기도
  전에 EntityManagerFactory 를 사용하는 객체가 있어 예외를 발생한다.
- 이 경우, Jpa는 SpringContainedBean 에서 Bean을 등록하는데 BeanCreate 를 실패하면 newInstance 를 생성하고 실제 
  EntityListener 가 빈이 아니라 새로운 객체가 할당된다.

(참고 : https://kangwoojin.github.io/programing/jpa-entity-listeners/)

<br>

**AccountHistoryListenerV3**

```
public class AccountHistoryListenerV3 {

    @Lazy
    @Autowired
    private AccountHistoryServiceV3 accountHistoryServiceV3;

    @PostPersist
    public void postPersist(AccountV3 account) {
        AccountHistoryRequestDto accountHistoryRequestDto = AccountHistoryRequestDto.create(
                account.getAccountNumber(),
                account.getBalance(),
                LocalDateTime.now()
        );

        accountHistoryServiceV3.addAccountHistory(accountHistoryRequestDto);
    }
    ...    
}

```

<br>

### 3. EntityListener 에서 event 를 선언하는 방법

- **ApplicationEventPublisher : 어플리케이션 내부에서 Event 를 publish 하는 객체**
    - **spring version < 4.2**
        - 이벤트 수신하는 객체를 만들기 위해 **ApplicationListener<T>** 의 구현체를 생성해야 했다.
        - 이벤트를 만들기 위해서는 **ApplicationEvent** 를 상속받아서 구현해야 했다.
    - **spring version >= 4.2**
        - **ApplicationEventPublisher.publishEvent(Object event)** 메서드가 추가되면서
          더이상 이벤트 객체를 생성하는데 ApplicationEvent 를 상속받을 필요가 없다.
        - **@EventListener** 가 도입되며 ApplicationListener<T> 의 하위 구현체 없이도
          이벤트 메세지를 전달받아 콜백함수를 호출할 수 있게 되었다.

<br>

**AccountHistoryListenerV1**

```java
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

        applicationEventPublisher.publishEvent(accountHistoryRequestDto);
    }
}
```

**AccountHistoryServiceV1**

```java

@Service
@RequiredArgsConstructor
public class AccountHistoryServiceV1 {

    private final AccountHistoryRepositoryV1 accountHistoryRepositoryV1;

    @EventListener
    public void addAccountHistory(AccountHistoryRequestDto accountHistoryRequestDto) {
        AccountHistoryV1 accountHistoryV1 = AccountHistoryV1.create(
                accountHistoryRequestDto.getAccountNumber(),
                accountHistoryRequestDto.getBalance(),
                accountHistoryRequestDto.getCreatedTime()
        );
        
        accountHistoryRepositoryV1.save(accountHistoryV1);
        log.debug("accountHistory added: {}", accountHistoryV1);
    }

}
```

## 3. EntityListener 를 통한 방법의 한계

1. 같은 LifeCycle 의 콜백 함수가 존재할 경우 우선순위에 의해 하나만 동작한다.
   - 예를 들어, AuditingEntityListener 와 같은 콜백함수가 존재하면 우선순위에 의해 동작하지 않을 수도 있다.
    (e.g. @CreatedDate)
   - 참고 : Hibernate Docs(https://docs.jboss.org/hibernate/stable/entitymanager/reference/en/html/listeners.html)
2. ApplicationEventPublisher 를 사용하는 경우, 복잡한 요구사항 핸들링이 어려울 수가 있다.(e.g. 메세지 유실, 메세지 관리)
   - 이를 해결하는 방법 중 하나는 **RabbitMQ, Kafka 와 같은 미들웨어를 도입하는 방법**을 고민해 볼 수 있다.
   - 미들웨어를 도입하는 방법은 복잡한 비즈니스 문제를 해결할 수 있는 장점이 있지만 구현이 복잡해질 수 있는 단점이 존재한다.