package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired BasicService basicService;

    @Test
    void proxyCheck(){
        log.info("aop class={}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest(){
        basicService.tx();
        basicService.noneTx();
    }

    @TestConfiguration
    static class TxApplyBasicConfig{
        @Bean
        BasicService basicService(){
            return new BasicService();
        }
    }

    /**
     * 트랜잭션은 실제 객체 대신에 프록시 객체가 대신 들어가 있다.
     * 프록시는 BasicService를 상속받아 만들어진다.
     *
     * `@Transactional이 있으면 트랜잭션을 활성화하게 된다.
     *
     * class 레벨로 올라간다면 밑의 모든 메서드가 트랜잭션이 적용된다.
     */
    @Slf4j
    static class BasicService{
        @Transactional
        public void tx(){
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
        }

        public void noneTx(){
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
        }
    }
}
