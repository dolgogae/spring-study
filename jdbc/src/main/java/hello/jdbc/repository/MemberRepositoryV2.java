package hello.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Jdbc;
import org.springframework.jdbc.support.JdbcUtils;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

/**
 * JDBC - ConnectionParam
 */
@Slf4j
public class MemberRepositoryV2 {

    // 추상화된 인터페이스를 쓰기 때문에 구현체가 바뀌어도 사용하는 코드에는 변경이 없다.
    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource){
        this.dataSource = dataSource;
    }
    
    public Member save(Member member) throws SQLException{
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
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findById(String memberId) throws SQLException{
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
            log.error("db error", e);
            throw e;
        } finally{
            close(con, pstmt, rs);
        }
    }

    
    public Member findById(Connection con, String memberId) throws SQLException{
        String sql = "select * from member where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
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
            log.error("db error", e);
            throw e;
        } finally{
            // connection은 여기서 닫지 않는다. -> 여기서 종료해버리면 연결이 안되고 끝난다.
            // 서비스에서 종료해야 한다.
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    public void update(String memberId, int money) throws SQLException{
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
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void update(Connection con, String memberId, int money) throws SQLException{
        String sql = "update member set moeny=? where member_id=?";

        PreparedStatement pstmt = null;

        try{
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("result size = {}", resultSize);
        } catch (SQLException e){
            log.error("db error", e);
            throw e;
        } finally {
            JdbcUtils.closeStatement(pstmt);
        }
    }


    public void delete(String memberId) throws SQLException{
        String sql = "delete from member where member_id=?";

        Connection con = null;        
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e){
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs){
        
        JdbcUtils.closeConnection(con);
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);

    }
    
    private Connection getConnection() throws SQLException{
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}