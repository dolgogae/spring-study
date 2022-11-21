package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager txManager;

    @TestConfiguration
    static class BasicTxTestConfig{
        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource){
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit(){
        log.info("트랜잭션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 커밋 시작");
        txManager.commit(status);
        log.info("트랜잭션 커밋 완료");
    }

    @Test
    void rollback(){
        log.info("트랜잭션 시작");
        TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 롤백 시작");
        txManager.rollback(status);
        log.info("트랜잭션 커밋 완료");
    }

    /**
     * 로그를 보면 tx1,2가 같은 커넥션을 사용했다.
     * 왜냐하면 커넥션 풀에서 가져오기 때문이다. 물리적인 커넥션은 같지만 다른 커넥션이라고 이해하면 좋다.
     *
     * 히카리 커넥션풀에서 커넥션 획득시에는 히카리 프록시 커넥션이라는 객체를 생성해서 반환한다.
     * 커넥션은 그대로지만 프록시 객체는 새로 생성하기 때문에 이것을 구분하기 위해서는 프록시 객체의 주소를 확인해주면 된다.
     */
    @Test
    void doubleCommit(){
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        txManager.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 커밋");
        txManager.commit(tx2);
    }

    /**
     * 같은 커넥션이지만 전혀 서로에게 영향을 주지 않는다.
     */
    @Test
    void doubleCommitRollback(){
        log.info("트랜잭션1 시작");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        txManager.commit(tx1);

        log.info("트랜잭션2 시작");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 롤백");
        txManager.rollback(tx2);
    }

    /**
     * 외부 트랜잭션이 끝나기전에 내부 트랜잭션이 시작되었다.
     * 내부 트랜잭션은 외부 트랜잭션에 참여하게 된다.
     * => 외부 트랜잭션과 내부 트랜잭션이 하나의 물리 트랜잭션으로 묶이게 된다.
     *
     * 외부 트랜잭션 시작
     * Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * Acquired Connection [HikariProxyConnection@905064175 wrapping conn0: url=jdbc:h2:mem:d788e4bc-219b-4c6d-b47d-e9cdd69f4b1f user=SA] for JDBC transaction
     * Switching JDBC Connection [HikariProxyConnection@905064175 wrapping conn0: url=jdbc:h2:mem:d788e4bc-219b-4c6d-b47d-e9cdd69f4b1f user=SA] to manual commit
     * outer.isNewTransaction()=true
     * 내부 트랜잭션 시작
     * Participating in existing transaction
     * inner.isNewTransaction()=false
     *
     * 내부 트랜잭션 커밋
     * ====> 이 시점에서는 아무 일도 일어나지 않는다. 만약 커밋해버리면 트랜잭션이 끝나버린다.
     *
     * 외부 트랜잭션 커밋
     * Initiating transaction commit
     * Committing JDBC transaction on Connection [HikariProxyConnection@905064175 wrapping conn0: url=jdbc:h2:mem:d788e4bc-219b-4c6d-b47d-e9cdd69f4b1f user=SA]
     * Releasing JDBC Connection [HikariProxyConnection@905064175 wrapping conn0: url=jdbc:h2:mem:d788e4bc-219b-4c6d-b47d-e9cdd69f4b1f user=SA] after transaction
     *
     * 트랜잭션 매니저는 트랜잭션 동기화 매니저를 통해서 기존 트랜잭션이 존재하는지 확인한다.
     * 트랜잭션이 있다면 기존 트랜잭션에 참여하게된다.(현재 트랜잭션은 아무것도 하지 않겠다는 뜻이다. - 로그 하나만 남겨준다.)
     *
     * 내부 트랜잭션에서 커밋을 호출하면 신규 트랜잭션인지의 여부(isNewTransaction())를 확인하고 신규 트랜잭션이 아니라면 실제 커밋을 호출하지 않는다.
     * 마지막으로 외부 트랜잭션을 커밋하면 신규 트랜잭션을 확인받아 진짜 커밋을 하게된다.
     */
    @Test
    void inner_commit(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());
        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer);
    }

    /**
     * 외부 트랜잭션 시작
     * Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * Acquired Connection [HikariProxyConnection@109193638 wrapping conn0: url=jdbc:h2:mem:c5128b55-e706-4239-8e8a-d864ab2b17c5 user=SA] for JDBC transaction
     * Switching JDBC Connection [HikariProxyConnection@109193638 wrapping conn0: url=jdbc:h2:mem:c5128b55-e706-4239-8e8a-d864ab2b17c5 user=SA] to manual commit
     * 내부 트랜잭션 시작
     * Participating in existing transaction
     * 내부 트랜잭션 커밋
     * 외부 트랜잭션 롤백
     * Initiating transaction rollback
     * Rolling back JDBC transaction on Connection [HikariProxyConnection@109193638 wrapping conn0: url=jdbc:h2:mem:c5128b55-e706-4239-8e8a-d864ab2b17c5 user=SA]
     *
     * 위에서의 로그처럼 모두 롤백된다.
     */
    @Test
    void outer_rollback(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 커밋");
        txManager.commit(inner);

        log.info("외부 트랜잭션 롤백");
        txManager.rollback(outer);
    }

    /**
     * 외부 트랜잭션 시작
     * Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * Acquired Connection [HikariProxyConnection@1486209125 wrapping conn0: url=jdbc:h2:mem:042f4d08-dc94-42c6-942f-040349894f10 user=SA] for JDBC transaction
     * Switching JDBC Connection [HikariProxyConnection@1486209125 wrapping conn0: url=jdbc:h2:mem:042f4d08-dc94-42c6-942f-040349894f10 user=SA] to manual commit
     * 내부 트랜잭션 시작
     * Participating in existing transaction
     * 내부 트랜잭션 롤백
     * Participating transaction failed - marking existing transaction as rollback-only       ====> 기존 트랜잭션에 롤백을 마킹 해두었다.
     * Setting JDBC transaction [HikariProxyConnection@1486209125 wrapping conn0: url=jdbc:h2:mem:042f4d08-dc94-42c6-942f-040349894f10 user=SA] rollback-only
     * 외부 트랜잭션 커밋
     * Global transaction is marked as rollback-only but transactional code requested commit  ====> 최종적으로 롤백을 결정하게 된다.
     * Initiating transaction rollback
     *
     * 어딘가 내부 트랜잭션에서 롤백을 표시해두면 마지막 커밋에서 롤백 마킹을 보고 물리 트랜잭션을 롤백을 결정하게 된다.
     *
     * 트랜잭션 매니져는 개발자가 의도한 것은 commit인데 최종 결과를 rollback 되었다는 것을 알려주어야한다. 이것을 UnexpectedRollbackException으로 알려주게 된다.
     * 이러한 방식으로 프로그램의 모호함을 해결해주었다.
     */
    @Test
    void inner_rollback(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백");
        txManager.rollback(inner); // rollback-only로 마킹해놓는다.

        log.info("외부 트랜잭션 커밋");
        Assertions.assertThatThrownBy(() -> txManager.commit(outer))
                .isInstanceOf(UnexpectedRollbackException.class);
    }

    /**
     * 다음처럼 REQUIRES_NEW 옵션을 주면 새로운 트랜잭션을 만들게 되어
     * 서로의 논리 트랜잭션이 영향을 미치지 않는다.
     */
    @Test
    void inner_rollback_requires_new(){
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);      // 신규 트랜잭션 시작, 기존 트랜잭션을 사용하지 않는다.
        TransactionStatus inner = txManager.getTransaction(definition);
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

        log.info("내부 트랜잭션 롤백");
        txManager.rollback(inner);          // 신규 트랜잭션은 종료된다.

        log.info("외부 트랜잭션 커밋");
        txManager.commit(outer);
    }
}