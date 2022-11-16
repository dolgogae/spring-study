package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * use JdbcTemplate
 * 
 * JdbcTemplate은 JDBC사용시 반복되는 코드를 대부분 해결해줄수 있다.
 */
@Slf4j
public class MemberRepositoryV5 implements  MemberRepository {

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) value (?, ?)";
        template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) ->{
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }

    public void update(String memberId, int money) {
        String sql = "update member set moeny=? where member_id=?";
        template.update(sql, money, memberId);
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }
}