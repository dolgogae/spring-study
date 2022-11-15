package hello.jdbc.service;

import java.sql.SQLException;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {
    
    private final MemberRepositoryV1 memberRepository;

    /**
     * 트랜잭션은 비즈니스로직의 시작에서 걸어줘야 한다. 
     * 비즈니스 로직에서 잘못 될 경우에 그 전체를 함께 롤백을 해주어야 하기 때문이다.
     * 
     * 트랜잭션을 시작하려면 커넥션이 필요하다.
     * 트랜잭션 내에서는 동일한 세션으로 해줘야한다. 밑의 메서드에서 다른 세션이게 된다면 원자성, 정합성을 보장하지 못한다.
     */ 
    public void accountTransfer(String fromId, String toId, int money) throws SQLException{
        // 트랜잭션 시작
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
        // 커밋, 롤백
    }

    private void validation(Member toMember){
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
