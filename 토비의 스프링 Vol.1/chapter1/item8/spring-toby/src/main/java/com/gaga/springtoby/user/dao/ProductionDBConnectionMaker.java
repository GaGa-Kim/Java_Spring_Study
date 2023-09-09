package com.gaga.springtoby.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ConnectionMaker 구현 클래스
 */
public class ProductionDBConnectionMaker implements ConnectionMaker {
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        // 운영용 DB Connection 생성코드
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost/toby", "toby", "gaga");
        return c;
    }
}