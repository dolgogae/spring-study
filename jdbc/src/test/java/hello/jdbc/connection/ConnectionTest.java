package hello.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.extern.slf4j.Slf4j;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {
    
    @Test
    void driverManager() throws SQLException{
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("connection = {}, class={}", con1, con1.getClass());
        log.info("connection = {}, class={}", con2, con2.getClass());
    }

    @Test
    void dataSourceDriverManager() throws SQLException{
        // DriverManagerDataSource: 항상 새로운 커넥션을 획득
        // DriverManager같은 경우에는 URL, USERNAME, PASSWORD를 매번 연결마다 전달하면 된다.
        // 하지만 DriverManagerDataSource는 getConnection만 호출해주면 된다.
        // 설정을 한곳에서 하지만 사용은 여러곳에서 하는것이 가능하다.
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException{
        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        // 만약 10개를 넘어가게 된다면 connection을 맺을때까지 
        // 대기를 하다가 최대시간 설정이 넘어가면 에러가 떨어진다.
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }

    private void useDataSource(DataSource dataSource) throws SQLException{
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("connection = {}, class={}", con1, con1.getClass());
        log.info("connection = {}, class={}", con2, con2.getClass());
    }
}
