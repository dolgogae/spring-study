package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

/**
 * 왜 체크 예외는 커밋하고 언체크 예외는 롤백할까?
 * 체크예외는 비즈니스 의미가 있을때 사용하고, 언체크는 복구 불가능한 예외로 가정한다.
 *
 * 만약 결제 잔고가 부족해서 NotEnoughMoneyException이라는 체크 예외가 발생한다고 가정하면
 * 고객의 잔고 부족은 시스템에 문제가 있는 것이 아니다.
 */
@SpringBootTest
public class RollbackTest {

    @Autowired RollbackService service;

    /**
     * o.s.orm.jpa.JpaTransactionManager        : Initiating transaction rollback
     */
    @Test
    void runtimeException(){
        assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    /**
     * o.s.orm.jpa.JpaTransactionManager        : Committing JPA transaction on EntityManager [SessionImpl(184147252<open>)]
     */
    @Test
    void checkdException(){
        assertThatThrownBy(()-> service.checkException())
                .isInstanceOf(MyException.class);
    }

    /**
     * o.s.orm.jpa.JpaTransactionManager        : Initiating transaction rollback
     */
    @Test
    void rollbackFor(){
        assertThatThrownBy(()-> service.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig{

        @Bean
        RollbackService rollbackService(){
            return new RollbackService();
        }
    }

    @Slf4j
    static class RollbackService{

        // 런타임 예외 발생: 롤백
        @Transactional
        public void runtimeException(){
            log.info("call runtimeException");
            throw new RuntimeException();
        }

        // 체크 예외 발생: 커밋
        @Transactional
        public void checkException() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }

        // 체크 예외 rollbackFor 지정 : 롤백
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("call checkedException");
            throw new MyException();
        }
    }

    static class MyException extends Exception{

    }
}
