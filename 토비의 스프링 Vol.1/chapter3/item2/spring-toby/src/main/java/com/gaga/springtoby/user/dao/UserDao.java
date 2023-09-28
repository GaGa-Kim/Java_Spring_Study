package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

/**
 * JDBC를 이용한 등록과 조회 기능이 있는 UserDao 클래스
 */
public class UserDao {
    // UserDao에 주입될 의존 오브젝트의 타입을 ConnectionMaker에서 DataSource로 변경한다.
    private DataSource dataSource;

    // DataSource 인터페이스 적용
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 메소드로 분리한 try/catch/finally 컨텍스트 코드
    // 클라이언트로부터 StatementStrategy 타입의 전략 오브젝트를 제공받게 된다.
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        // JDBC try/catch/finally 구조로 만들어진 컨텍스트 내에서 작업을 수행한다.
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
        } catch (SQLException e) {
            throw  e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /* 새로운 사용자를 생성 */
    // JDBC API가 만들어내는 예외를 잡아서 직접 처리하거나, 메소드에 throws를 선언해서 예외가 발생하면 메소드 밖으로 던지게 한다.
    public void add(User user) throws SQLException {

        // DB 연결을 위한 Connection을 가져온다.
        Connection c = dataSource.getConnection();

        // SQL을 담은 Statement를 만든다.
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        // 만들어진 Statement를 실행한다.
        ps.executeUpdate();

        ps.close();
        c.close();
    }

    /* 아이디를 가지고 사용자 정보 읽어오기 */
    public User get(String id) throws SQLException {

        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        // SQL 쿼리의 실행 결과를 ResultSet으로 받아서 정보를 저장할 오브젝트에 옮겨준다.
        ResultSet rs = ps.executeQuery();

        // User는 null 상태로 초기화해놓는다.
        User user = null;
        // id를 조건으로 한 쿼리의 결과가 있으면 User 오브젝트를 만들고 값을 넣어준다.
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        // 결과가 없으면 User는 null 상태 그대로일 것이므로 이를 확인해서 예외를 던져준다.
        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    /* 모든 사용자 삭제하기 */
    // 전략 오브젝트를 만들고 컨텍스트를 호출하는 클라이언트 책임을 담당한다.
    public void deleteAll() throws SQLException {

        // 선정한 전략 클래스의 오브젝트를 생성한다.
        // 컨텍스트가 필요로 하는 전략의 특정 구현 오브젝트를 클라이언트가 만들어서 제공
        StatementStrategy st = new DeleteAllStatement();

        // 컨텍스트를 호출하여 전략 오브젝트를 전달한다.
        jdbcContextWithStatementStrategy(st);
    }

    /* 사용자 테이블의 레코드 개수 읽어오기 */
    public int getCount() throws SQLException {

        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // ResultSet도 다양한 SQLException이 발생할 수 있는 코드이므로 try 블록 안에 둬야 한다.
        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try{
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}