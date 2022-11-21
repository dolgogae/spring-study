package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;

/**
 * 대상객체 내부에서 호출하게 된다면 @Transactional이 있어도 트랜잭션이 적용되지 않는다.
 */
@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }

    /**
     * 이것은 프록시 객체를 통해서 호출해주기 때문에
     * 트랜잭션이 문제없이 적용된다.
     */
    @Test
    void internalCall(){
        callService.internal();
    }

    /**
     * 다음처럼 호출하게 되면 @Transactional이 트랜잭션을 걸어주지 않는다.
     * 왜냐하면 프록시 객체를 통해서 호출하는 것이 아닌 내부적으로 호출하기 때문에
     * 트랜잭션을 거는 AOP가 걸리지 않는 것이다.
     *
     * 2022-11-21 15:43:00.168  INFO 14184 --- [    Test worker] h.s.a.InternalCallV1Test$CallService     : call external
     * 2022-11-21 15:43:00.169  INFO 14184 --- [    Test worker] h.s.a.InternalCallV1Test$CallService     : tx Active = false
     * 2022-11-21 15:43:00.169  INFO 14184 --- [    Test worker] h.s.a.InternalCallV1Test$CallService     : tx readOnly = false
     * 2022-11-21 15:43:00.170  INFO 14184 --- [    Test worker] h.s.a.InternalCallV1Test$CallService     : call internal
     * 2022-11-21 15:43:00.171  INFO 14184 --- [    Test worker] h.s.a.InternalCallV1Test$CallService     : tx Active = false
     * 2022-11-21 15:43:00.171  INFO 14184 --- [    Test worker] h.s.a.InternalCallV1Test$CallService     : tx readOnly = false
     */
    @Test
    void externalCall(){
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig{

        @Bean
        CallService callService(){
            return new CallService();
        }
    }

    /**
     * external을 호출하고 그 내부에서 internal을 호출하는 구조로 설계된 클래스이다.
     * external은 트랜잭션이 필요없고, internal은 트랜잭션이 필요한 상황이다.
     *
     * 가장 단순한 방법으로 internal 메서드를 별도의 클래스로 분리하는 해결방법이 있다.
     */
    @Slf4j
    static class CallService{

        public void external(){
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active = {}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", readOnly);
        }
    }
}
