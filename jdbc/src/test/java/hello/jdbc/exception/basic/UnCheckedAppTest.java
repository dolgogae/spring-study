package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.naming.ldap.Control;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void checked(){
        Controller controller = new Controller();
        assertThatThrownBy(()->controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    void printEx(){
        Controller controller = new Controller();
        try{
            controller.request();
        } catch (Exception e){
            log.info("ex", e);
        }
    }

    static class Controller{
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }
    
    /**
     * SQLException에 대한 의존관계가 없어졌다.
     * 복구 불가능한 예외에 대해서 신경쓰지 않을 수 있다.
     */
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient{
        public void call() {
            throw new RuntimeConnectException("connect failed.");
        }
    }

    static class Repository{
        public void call() {
            try{
                runSql();
            } catch (SQLException e){
                // change runtimeError
                throw new RuntimeSQLException(e);
            }
        }

        public void runSql() throws SQLException{
            throw new SQLException("ex");
        }

    }

    static class RuntimeSQLException extends RuntimeException {
        /**
         * 기존의 exception을 가지고 있게 만들어 주었다.
         * 새로 생성된 예외에 기존의 예외가 포함하게 만들어 주어야한다.
         * 어떤 것 때문에 RuntimeException이 발생한지 알 수가 없게 된다.(원인을 찾을 수 없다.)
         */
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }
}
