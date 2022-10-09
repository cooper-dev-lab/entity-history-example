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

(참고 : https://javaslave.tistory.com/50 )

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
- EntityListener 에서 EntityManagerFactory 연관된 빈을 주입하는데 주의사항
  1. EntityFactoryManager 를 Bean으로 등록할 때 EntityListener 에 대해 Bean을 등록하는
    작업이 존재하는데, EntityListener 에서 EntityManagerFactory 를 사용하는 Bean 이 존재하면 문제가 발생한다. 
  2. EntityManagerFactory 를 사용하는 Repository component 가 존재할 경우, EntityManagerFactory 를 생성하기도
    전에 EntityManagerFactory 를 사용하는 객체가 있어 예외를 발생한다.
  3. 이 경우, Jpa는 SpringContainedBean 에서 Bean을 등록하는데 BeanCreate 를 실패하면 newInstance 를 생성하고 실제 
    EntityListener 가 빈이 아니라 새로운 객체가 할당된다.

(참고 : https://kangwoojin.github.io/programing/jpa-entity-listeners/)

<br>

**AccountHistoryListenerV3**

```java
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
- 주의사항!!
  - ApplicationEventPublisher 는 **동기적으로 동작**한다. 즉, ApplicationEventPublisher 가 보낸 메세지에 과한 동작이 완료되고 나서,
    다음 동작을 실행한다. 그러므로, 별도의 쓰레드르르 통해 비동기적으로 처리하고 싶다면 `@Async` 를 추가하도록 한다.  

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
   - 미들웨어를 도입하는 방법은 고가용성의 장점이 있지만 구현이 복잡해질 수 있는 단점이 존재한다.
     (keyword : back pressure : publisher - subscriber 의 대응을 보조해주는 역할)
     - 가용성(Availability) : 시스템이 정상적으로 사용 가능한 정도
   - 추가적으로 이벤트를 전달하는 방식에는 WebFlux 가 있다. (e.g. Mono, Flux)

<br>

## 4. Hibernate listener 등록하는 방법

### 1. org.hibernate.SessionFactory

![image](https://user-images.githubusercontent.com/48561660/194755125-b5cb379e-0273-43cb-a7fa-8f60348e6426.png)
(**SessionFactory 다이어그램**)

1. 하이버네이트는 기본적으로 DB Connection 을 래핑한 **`org.hibernate.Session`** 단위로 동작힌다.
2. 이 때, SessionFactory 는 Session 을 생성하는 팩토리 역할을 하며, 하위 구현체로 `SessionFactoryImpl` 이 있다. 
   다이어그램을 확인해보면 SessionFactory 의 상위 인터페이스로 `EntityManagerFactory` 가 존재하는 것을 확인할 수 있다. 
   즉, EntityManagerFactory 역할을 SessionFactoryImpl 클래스가 하는 것을 알 수 있다.

<br>

**AccountLogListenerConfig**

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AccountHistoryConfig implements InitializingBean {

    private final EntityManagerFactory entityManagerFactory;

    private final AccountHistoryListener accountHistoryListener;

    @Override
    public void afterPropertiesSet() {
        addEventListenerInSessionFactory();
    }

    private void addEventListenerInSessionFactory() {
        SessionFactoryImpl sessionFactoryImpl = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry eventListenerRegistry
                = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        eventListenerRegistry.appendListeners(EventType.POST_INSERT, accountHistoryListener);
    }

}
```

**AccountHistoryListener**

```java
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
```


 
- https://docs.jboss.org/hibernate/orm/5.6/userguide/html_single/Hibernate_User_Guide.html
- https://medium.com/@rachit.dixit/the-magic-of-hibernate-listeners-with-spring-boot-47b61ef60bd4