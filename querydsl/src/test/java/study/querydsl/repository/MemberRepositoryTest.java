package study.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {


    @Autowired
    EntityManager em;
    @Autowired MemberRepository memberRepository;

    @Test
    public void basicTest(){
        Member member = new Member("member1", 10);
        memberRepository.save(member);

        Member result1 = memberRepository.findById(member.getId()).get();
        assertThat(result1).isEqualTo(member);

        List<Member> result2 = memberRepository.findAll();
        assertThat(result2).containsExactly(member);

        List<Member> result3 = memberRepository.findByUsername(member.getUsername());
        assertThat(result3).containsExactly(member);
    }

}
