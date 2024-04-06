package com.gaga.springtoby.user.sqlService;

import java.util.Map;

/**
 * SQL 수정 기능을 가진 확장 인터페이스
 */
public interface UpdatableSqlRegistry extends SqlRegistry {
	void updateSql(String key, String sql) throws SqlUpdateFailureException;

	void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
