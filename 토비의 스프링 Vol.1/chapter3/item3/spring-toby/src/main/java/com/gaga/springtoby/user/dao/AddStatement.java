package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * add 메소드의 기능을 구현한 StatementStrategy 전략 클래스
 */
public class AddStatement implements StatementStrategy {

    // 부가적인 정보인 user가 필요하다.
    User user;

    // 클라이언트로부터 User 타입의 오브젝트를 받을 수 있도록 AddStatement의 생성자를 통해 제공받는다.
    public AddStatement(User user) {
        this.user = user;
    }

    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        return ps;
    }
}
