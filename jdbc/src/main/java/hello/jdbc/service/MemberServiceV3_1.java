package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * transaction manager
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    
//    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException{
        // start transaction
        // 트랜잭션을 시작하려면 데이터베이스 커넥션이 필요하다. -> transactionManager는 내부적으로 DataSource를 가지고 있다.(생성자를 통해)
        // 커넥션을 수동 커밋 모드로 변경후 실제 데이터베이스 트랜잭션을 시작한다.
        // 커넥션을 트랜잭션 동기화 매니저에 보관한다. + (con.setAutoCommit(false))
        // 트랜잭션 동기화 매니저는 스레드 로컨에 커넥션을 보관하여 멀티 스레드 환경에 안전하게 커넥션 보관
        // 커넥션 호출(MemberRepositoryV3.getConnection 소스코드 확인)시에 데이터 접근 로직(현재 예시는 JDBC)에서 앞선 트랜잭션 동기화 매니저에서 저장된 커넥션을 가져와 사용하게 된다. 
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            bizLogic(fromId, toId, money);
            transactionManager.commit(status);   // 성공시 커밋
        }catch(Exception e){
            transactionManager.rollback(status); // 실패시 롤백
            throw new IllegalStateException(e);
        }finally{
            // release takes care of it from transaction manager
            // The repository automatically returns the connection to the <data synchronization manager>, so release is not required.
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException{
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById( toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);

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
