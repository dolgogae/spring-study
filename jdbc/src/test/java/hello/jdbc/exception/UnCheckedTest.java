package hello.jdbc.exception;

import com.jayway.jsonpath.spi.cache.Cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import hello.jdbc.exception.CheckedTest.Repository;
import hello.jdbc.exception.CheckedTest.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnCheckedTest {

    @Test
    void unCheckedCatch(){
        Service service = new Service();
        service.callCactch();
    }

    @Test
    void unCheckedThrow(){
        Service service = new Service();

        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }
    
    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message){
            super(message);
        }
    }

    static class Repository{
        /**
         * throws를 선언하지 않아도 된다.(생략가능)
         */
        public void call(){
            throw new MyUncheckedException("ex");
        }
    }
    
    /**
     * Unckecked 예외는
     * 에외를 잡거나, 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다.
     */
    static class Service{
        Repository repository = new Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리하면 된다.
         */
        public void callCactch(){
            try{
                repository.call();
            } catch(MyUncheckedException e){
                log.info("예외처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지않아도 자연스럽게 상위로 넘어간다.
         * 체크 예외와 다르게 thorws 예외 선언을 하지 않아도 된다.
         */
        public void callThrow(){
            repository.call();
        }
    }
}
