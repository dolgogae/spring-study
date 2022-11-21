package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;

/**
 * 스프링에서 public인 메서드만 트랜잭션을 허용했다.
 * 왜냐하면 내부 메서드 호출을 프록시를 통해서 호출되지도 않고,
 * 필요없는 것까지 과도하게 트랜잭션이 걸릴수 있기 때문이다.
 *
 * 추가적으로 public이 비즈니스 로직의 시작이 되기 때문에 보통 트랜잭션이 필요하고 나머지는 필요한 경우가 많지 않다는 점도 있다.
 */
@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired CallService callService;

    @Test
    void printProxy(){
        log.info("callService class={}", callService.getClass());
    }

    /**
     * 2022-11-21 15:53:35.362  INFO 14612 --- [    Test worker] h.s.a.InternalCallV2Test$CallService     : call external
     * 2022-11-21 15:53:35.362  INFO 14612 --- [    Test worker] h.s.a.InternalCallV2Test$CallService     : tx Active = false
     * 2022-11-21 15:53:35.362  INFO 14612 --- [    Test worker] h.s.a.InternalCallV2Test$CallService     : tx readOnly = false
     * 2022-11-21 15:53:35.445 TRACE 14612 --- [    Test worker] o.s.t.i.TransactionInterceptor           : Getting transaction for [hello.springtx.apply.InternalCallV2Test$InternalService.internal]
     * 2022-11-21 15:53:35.457  INFO 14612 --- [    Test worker] hello.springtx.apply.InternalCallV2Test  : call internal
     * 2022-11-21 15:53:35.457  INFO 14612 --- [    Test worker] hello.springtx.apply.InternalCallV2Test  : tx Active = true
     * 2022-11-21 15:53:35.458  INFO 14612 --- [    Test worker] hello.springtx.apply.InternalCallV2Test  : tx readOnly = false
     *
     * 다음과 같이 internal을 별도로 만들어주었기 때문에 프록시를 호출해 정상적으로 트랜잭션을 호출해줬다.
     * 별도의 클래스로 분리하는 방법이 실무에서 가장 합리적이다.
     */
    @Test
    void externalCall(){
        callService.external();
    }

    @TestConfiguration
    static class InternalCallV1TestConfig{

        @Bean
        CallService callService(){
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService(){
            return new InternalService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService{

        private final InternalService internalService;

        public void external(){
            log.info("call external");
            printTxInfo();
            internalService.internal();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active = {}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", readOnly);
        }
    }

    static class InternalService{

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
