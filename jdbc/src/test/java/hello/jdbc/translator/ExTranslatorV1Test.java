package hello.jdbc.translator;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import hello.jdbc.repository.ex.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

/**
 * 문제점
 * DB가 바뀔경우에 오류코드를 모두 찾아서 변경해주어야 한다.
 * SQL 문법에 오류가 있는 경우 수십 수백가지의 오류코드가 있다.
 */
public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySate(){
        service.create("myId");
        service.create("myId");
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service{
        private final Repository repository;

        public void create(String memberId){
            try{
                repository.save(new Member(memberId, 0));
                log.info("saveId={}", memberId);
            }catch (MyDuplicateKeyException e){
                log.info("duplicate keys, attempting to recover");
                String retryId = generateNewId(memberId);
                log.info("retry id = {}", retryId);
                repository.save(new Member(retryId, 0));
            }catch (MyDbException e){
                log.info("Data Access Layer Exception");
                throw e;
            }
        }

        private String generateNewId(String memberId){
            return memberId + new Random().nextInt(10000);
        }
    }


    @RequiredArgsConstructor
    static class Repository{
        private final DataSource dataSource;

        public Member save(Member member){
            String sql = "insert into member(member_id, moeny) values(?,?)";
            Connection con = null;
            PreparedStatement pstmt = null;

            try {
                con = dataSource.getConnection();
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            }catch (SQLException e) {
                //h2 db
                if(e.getErrorCode() == 23505){
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);
            } finally{
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(con);
            }
        }

    }
}
