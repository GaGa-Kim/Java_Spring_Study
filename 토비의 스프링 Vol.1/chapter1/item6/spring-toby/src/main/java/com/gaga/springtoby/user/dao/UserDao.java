package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;

import java.sql.*;

/**
 * JDBC를 이용한 등록과 조회 기능이 있는 UserDao 클래스
 */
public class UserDao {
    // 초기에 설정하면 사용 중에는 바뀌지 않는 읽기전용 인스턴스 변수
    private ConnectionMaker connectionMaker;
    /* 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수
       동시에 인스턴스 변수를 수정하면 매우 위험하며 심각한 문제 발생
    private Connection c;
    private User user;
     */

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    /* 새로운 사용자를 생성 */
    // JDBC API가 만들어내는 예외를 잡아서 직접 처리하거나, 메소드에 throws를 선언해서 예외가 발생하면 메소드 밖으로 던지게 한다.
    public void add(User user) throws ClassNotFoundException, SQLException {

        // DB 연결을 위한 Connection을 가져온다.
        Connection c = connectionMaker.makeConnection();
        /* 심각한 문제 발생
           this.c = connectionMaker.makeConnection();
         */

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
    public User get(String id) throws ClassNotFoundException, SQLException {

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        // SQL 쿼리의 실행 결과를 ResultSet으로 받아서 정보를 저장할 오브젝트에 옮겨준다.
        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        /* 심각한 문제 발생
        this.user = new User();
        this.user.setId(rs.getString("id"));
        this.user.setName(rs.getString("name"));
        this.user.setPassword(rs.getString("password"));
        */

        rs.close();
        ps.close();;
        c.close();

        return user;
        /* 심각한 문제 발생
        return this.user;
        */
    }
}