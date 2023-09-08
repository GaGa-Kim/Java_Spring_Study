package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 싱글톤 패턴을 적용한 UserDao (자바의 싱글톤 패턴)
 */
public class JavaUserDao {
    // 생성된 싱글톤 오브젝트를 저장할 수 있는 자신과 같은 타입의 static 필드를 정의한다.
    private static JavaUserDao INSTANCE;
    private ConnectionMaker connectionMaker;

    // 생성자
    private JavaUserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    /* 최초로 호출되는 시점에서 한 번만 오브젝트가 만들어지게 한다.
    public static synchronized JavaUserDao getInstance() {
        if (INSTANCE == null)
            // DaoFactory에서 UserDao를 생성하여 ConnectionMaker 오브젝트를 넣어주는 것이 불가능해진다.
            INSTANCE = new JavaUserDao(???);
        return INSTANCE;
    }
     */

    public void add(User user) throws ClassNotFoundException, SQLException {

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();;
        c.close();

        return user;

    }
}