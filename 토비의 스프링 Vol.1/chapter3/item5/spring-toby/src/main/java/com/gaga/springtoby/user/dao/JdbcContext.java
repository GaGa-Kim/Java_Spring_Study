package com.gaga.springtoby.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 콜백 재활용을 적용한 JdbcContext 클래스
 */
public class JdbcContext {

    private DataSource dataSource;

    // DataSource 타입 빈을 DI 받을 수 있게 준비해둔다.
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 템플릿 <<template>>
    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
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

    // 클라이언트 <<client>>
    // 변하지 않는 콜백 클래스 정의와 오브젝트 생성 부분을 분리시킨다.
    public void executeSql(final String query) throws SQLException {
        // 콜백 <<callback>>
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }
}
