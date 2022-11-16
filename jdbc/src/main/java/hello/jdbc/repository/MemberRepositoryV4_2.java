package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * add SQLExceptionTranslate
 * 
 * 마지막으로 JDBC의 반복 문제만 해결해주면 된다.
 * 
 */
@Slf4j
public class MemberRepositoryV4_2 implements  MemberRepository {

    // 추상화된 인터페이스를 쓰기 때문에 구현체가 바뀌어도 사용하는 코드에는 변경이 없다.
    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;

    public MemberRepositoryV4_2(DataSource dataSource){
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    /**
     * interface's method must declare exception
     * Depends on the particular implementation technology.(ex-SQLException)
     * @param member
     * @return
     * @throws SQLException
     */
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) value (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            return member;
        } catch (SQLException e){
            throw exTranslator.translate("save", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            // 컬럼명으로 해시맵같이 저장된다.
            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else{
                throw new NoSuchElementException("member not found memberId=" + memberId);
            }

        } catch(SQLException e){
            throw exTranslator.translate("findById", sql, e);
        } finally{
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) {
        String sql = "update member set moeny=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("result size = {}", resultSize);
        } catch (SQLException e){
            throw exTranslator.translate("update", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id=?";

        Connection con = null;        
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e){
            throw exTranslator.translate("delete", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        // 트랜잭션을 사용하기 위해 동기화된 커넥션은 닫지않고 그대로 유지해준다.
        DataSourceUtils.releaseConnection(con, dataSource);
    }
    
    private Connection getConnection() throws SQLException{
        // 만약 커넥션이 없다면 새로 만들어준다.
        // 이 작업을 통해서 같은 커넥션을 사용하게 된다.
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}