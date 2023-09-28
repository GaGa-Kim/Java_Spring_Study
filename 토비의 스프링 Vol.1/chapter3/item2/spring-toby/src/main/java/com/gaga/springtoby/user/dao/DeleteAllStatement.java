package com.gaga.springtoby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * makeStatement()를 구현한 StatementStrategy 전략 클래스
 */
public class DeleteAllStatement implements StatementStrategy {

    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps;
        ps = c.prepareStatement("delete from users");
        return  ps;
    }
}
