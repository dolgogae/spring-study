package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDto;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    
    @Autowired EntityManager em;
    @Autowired MemberJpaRepository memberJpaRepository;
    
    @Test
    public void basicTest(){
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member result1 = memberJpaRepository.findById(member.getId()).get();
        assertThat(result1).isEqualTo(member);

        List<Member> result2 = memberJpaRepository.findAll();
        assertThat(result2).containsExactly(member);

        List<Member> result3 = memberJpaRepository.findByUsername(member.getUsername());
        assertThat(result3).containsExactly(member);
    }

    @Test
    public void basicQuerydslTest(){

        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        List<Member> result2 = memberJpaRepository.findAll_Querydsl();
        assertThat(result2).containsExactly(member);

        List<Member> result3 = memberJpaRepository.findByUsername_Querydsl(member.getUsername());
        assertThat(result3).containsExactly(member);
    }

    @Test
    public void searchTest(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        /**
         * 조건문이 없으면 쿼리로 가져오는 데이터가 너무 많을수 있다.
         * 이런 경우에는 보통 페이징으로 해결한다.
         */
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

//        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        List<MemberTeamDto> result = memberJpaRepository.search(condition);
        assertThat(result).extracting("username").containsExactly("member4");
    }
}