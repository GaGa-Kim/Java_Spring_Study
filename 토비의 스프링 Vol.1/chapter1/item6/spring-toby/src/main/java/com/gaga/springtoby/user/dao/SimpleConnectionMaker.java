package com.gaga.springtoby.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 독립시킨 DB 연결 기능인 SimpleConnectionMaker 클래스
 */
public class SimpleConnectionMaker {
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        // 별도의 독립적인 DB connection 생성코드
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/toby", "toby", "gaga");
        return c;
    }
}
