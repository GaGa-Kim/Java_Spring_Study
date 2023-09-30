package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;
import com.gaga.springtoby.user.exception.DuplicateUserIdException;
import com.gaga.springtoby.user.exception.MysqlErrorNumbers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * JdbcTemplate를 적용한 UserDao 클래스
 */
public class UserDao {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    // JdbcTemplate 초기화
    public void setDataSource(DataSource dataSource) {
        // DataSource 오브젝트는 JdbcTemplate을 만든 후에는 사용하지 않으니 저장해두지 않아도 된다.
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        // SQLException 예외를 위해 사용
        this.dataSource = dataSource;
    }

    // 재사용 가능하도록 독립시킨 RowMapper
    private RowMapper<User> userMapper = new RowMapper<User>() {
        // ResultSet한 로우의 결과를 오브젝트에 매핑해주는 RowMapper
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    /* 새로운 사용자를 생성 */
    // JDBC API가 만들어내는 예외를 잡아서 직접 처리하거나, 메소드에 throws를 선언해서 예외가 발생하면 메소드 밖으로 던지게 한다.
    public void addWithException(User user) throws DuplicateUserIdException {

        Connection c = null;
        PreparedStatement ps = null;

        try {
            // JDBC를 이용해 user 정보를 DB에 추가하는 코드
            c = dataSource.getConnection();

            ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY)
                throw new DuplicateUserIdException(e); // 예외 전환
            else
                throw new RuntimeException(e); // 예외 포장
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /* 새로운 사용자를 생성 */
    // JdbcTemplate의 update() 메소드를 적용
    // 스프링의 JdbcTemplate은 예외처리 전략을 따르고 있으므로
    // JdbcTemplate 템플릿과 콜백 안에서 발생하는 모든 SQLException을 런타임 예외인 DataAccessException으로 포장해서 던져준다.
    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?,?,?)", user.getId(), user.getName(), user.getPassword());
    }

    /* 아이디를 가지고 사용자 정보 읽어오기 */
    // JdbcTemplate의 queryForObject() 메소드를 적용
    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id}, this.userMapper);
    }

    /* 모든 사용자 정보 읽어오기 */
    // JdbcTemplate의 query() 메소드를 적용
    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    /* 모든 사용자 삭제하기 */
    // JdbcTemplate의 update() 메소드를 적용
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    /* 사용자 테이블의 레코드 개수 읽어오기 */
    // JdbcTemplate의 queryForInt() 메소드를 적용 (Deprecated)
    // JdbcTemplate의 queryForObject() 메소드를 적용
    public int getCount() {
        // return this.jdbcTemplate.queryForInt("select count(*) from users");
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}