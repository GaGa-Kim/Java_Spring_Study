package com.gaga.springtoby.user.sqlService;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap을 이용하는 SqlRegistry 클래스
 */
public class HashMapSqlRegistry implements SqlRegistry {
	// sqlMap은 SqlRegistry 구현의 일부가 되므로 외부에서 직접 접근할 수 없다.
	private Map<String, String> sqlMap = new HashMap<String, String>();

	public String findSql(String key) throws SqlNotFoundException {
		String sql = sqlMap.get(key);
		if (sql == null)
			throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다");
		else
			return sql;
	}

	// HashMap이라는 저장소를 사용하는 구체적인 구현 방법에서 독립될 수 있도록 인터페이스의 메소드로 접근하게 해준다.
	public void registerSql(String key, String sql) {
		sqlMap.put(key, sql);
	}
}
