package com.gaga.springtoby.user.sqlService;

import java.util.Map;

/**
 * 맵을 이용한 SqlService의 구현
 */
public class SimpleSqlService implements SqlService {
	private Map<String, String> sqlMap;

	// 설정 파일에 <map>으로 정의된 SQL 정보를 가져오도록 프로퍼티에 등록해둔다.
	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		// 내부 SqlMap에서 SQL을 가져온다,
		String sql = sqlMap.get(key);
		if (sql == null)
			// 인터페이스에 정의된 규약대로 SQL을 가져오는데 실패하면 예외를 던지게 한다.
			throw new SqlRetrievalFailureException(key + "에 대한 SQL을 찾을 수 없습니다");
		else
			return sql;
	}
}
