package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@SpringBootTest
public class InitTxTest {

    @Autowired Hello hello;

    @Test
    void go(){
        // 초기화 코드 initV1는 스프링이 초기화 시점에 호출한다. - @PostConstruct
        // 이때 트랜잭션은 적용되지 않는다.

        // 하지만 initV2는 스프링이 모두 준비가 된 뒤에 초기화 코드가 실행 되어서 트랜잭션이 적용된다. - ApplicationReadyEvent
    }

    @TestConfiguration
    static class InitTxTestConfig{

        @Bean
        Hello hello(){
            return new Hello();
        }
    }

    @Slf4j
    static class Hello{

        @PostConstruct
        @Transactional
        public void initV1(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx active={}", isActive);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init ApplicationReadyEvent tx active={}", isActive);
        }
    }
}
