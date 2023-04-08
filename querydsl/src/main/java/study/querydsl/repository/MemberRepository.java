package study.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import study.querydsl.entity.Member;

import java.util.List;

/**
 * MemberRepositoryCustom을 통해서 QeuryDSL로 만든 기능을 import 받을 수 있다.
 *
 * 김영한 강사님 추천으로는 MemberQueryRepository로 별도의 클래스로 분리해서 작성하는 것이 좋다고 한다.
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, QuerydslPredicateExecutor<Member> {

    List<Member> findByUsername(String name);
}
