package com.example.examplespring.configuration.mybatis.typehandler;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * String Y, N ↔ boolean true, false 데이터 변환 핸들러
 * @author gagyeong
 */
public class StringValueBooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        // boolean 값이 true일 경우 Y, false일 경우 N로 바꿈
        preparedStatement.setString(i, BooleanUtils.toString(parameter, "Y", "N"));
    }

    @Override
    public Boolean getResult(ResultSet resultSet, String columnName) throws SQLException {
        // 자동으로 Y, N 값을 boolean으로 바꿈
        return BooleanUtils.toBoolean(resultSet.getString(columnName));
    }

    @Override
    public Boolean getResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return BooleanUtils.toBoolean(resultSet.getString(columnIndex));
    }

    @Override
    public Boolean getResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return BooleanUtils.toBoolean(callableStatement.getString(columnIndex));
    }
}
