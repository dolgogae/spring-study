package hello.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;


    /**
     * 비즈니스 로직 관련 소스보다는 트랜잭션 관련 소스가 너무 많다.
     * SQLException이나 connection을 반복해서 넘겨주는 것 등 JDBC의 소스코드에 너무 의존하게 된다.
     * 
     * 서비스계층은 순수해야한다.
     * 
     * 만약 데이터 접근 기술을 다른것으로(JPA, MyBatis) 바꾸게 된다면 관련된 모든 소스 코드를 고치면 된다.
     * 
     * 따라서 트랜잭션 또한 추상화를 통해 구현체를 받는게 좋다.
     */
    public void accountTransfer(String fromId, String toId, int money) throws SQLException{

        Connection con = dataSource.getConnection();
        try{
            con.setAutoCommit(false); // 트랜잭션 시작
            bizLogic(con, fromId, toId, money);
            con.commit();   // 성공시 커밋
        }catch(Exception e){
            con.rollback(); // 실패시 롤백
        }finally{
            release(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException{
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);

    }

    private void release(Connection con){
        if(con != null){
            try{
                con.setAutoCommit(true);        // 커넥션 풀 고려
                con.close();
            }catch(Exception e){
                log.info("error", e);
            }
        }
    }

    private void validation(Member toMember){
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
