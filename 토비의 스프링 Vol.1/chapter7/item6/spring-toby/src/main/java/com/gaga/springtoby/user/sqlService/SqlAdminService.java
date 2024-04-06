package com.gaga.springtoby.user.sqlService;

import javax.annotation.PostConstruct;

/**
 * SqlReader와 SqlRegistry를 사용하는 SqlService 구현 클래스
 */
public class SqlAdminService implements SqlService {
	protected SqlReader sqlReader;
	protected SqlRegistry sqlRegistry;

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	@PostConstruct
	public void loadSql() {
		// SqlReader에게 SqlRegistry를 전달하면서 SQL을 읽어서 저장해두도록 요청한다.
		this.sqlReader.read(this.sqlRegistry);
	}

	public String getSql(String key) throws SqlRetrievalFailureException {
		try {
			// SqlRegistry 타입 오브젝트에게 요청해서 SQL을 가져오게 한다.
			return this.sqlRegistry.findSql(key);
		} catch (SqlNotFoundException e) {
			throw new SqlRetrievalFailureException(e.getMessage());
		}
	}
}
