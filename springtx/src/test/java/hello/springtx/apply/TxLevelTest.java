package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxLevelTest {

    @Autowired LevelService levelService;

    @Test
    void orderTest(){
        levelService.write();
        levelService.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig{

        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }

    /**
     * 스프링에서 우선순위는 항상 더 구체적이고 자세한 것이 높은 우선순위를 가진다.
     * 클래스 단위의 어노테이션보단 메서드 단위의 어노테이션이 인터페이스보단 클래스가 우선순위가 높다.
     *
     * 아래에서 write는 메서드에 더 구체적으로 커스터마이즈 됐기 때문에 false가 적용된다.
     */
    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService{

        @Transactional(readOnly = false)
        public void write(){
            log.info("call write");
        }

        public void read(){
            log.info("call read");
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx Active = {}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", readOnly);
        }
    }
}
