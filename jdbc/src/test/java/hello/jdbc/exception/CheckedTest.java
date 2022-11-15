package hello.jdbc.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckedTest {

    @Test
    void checkedCatch(){
        Service service = new Service();
        service.callCactch();
    }

    @Test
    void checkedThrow(){
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
            .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception inheritence exception is checked exception
     */
    static class MyCheckedException extends Exception{
        public MyCheckedException(String message){
            super(message);
        }
    }

    /**
     * Check예외는 
     * 예외를 잡아서 처리하거나, 던지거나 둘중 하나를 필수적으로 처리해야한다.
     * 내가 잡을 예외만 명시적으로 잡는 것이 좋다. 
     * 
     * 상위의 예외를 던지게 되면 의도하지 않은 예외도 모두 잡힌다.
     */
    static class Service{
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCactch(){
            try{
                repository.call();
            }catch(MyCheckedException e){
                log.info("예외처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws 예외를 메서드에 필수로 선언해야한다.
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException{
            repository.call();
        }
    }

    static class Repository{
        // 예외의 경우 catch를 하지 않는다면 선언문에 throws를 이용해서 던진다고 알려줘야 한다.
        public void call() throws MyCheckedException{
            throw new MyCheckedException("ex");
        }
    }
}
