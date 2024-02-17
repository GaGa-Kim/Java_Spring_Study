package com.gaga.springtoby.user.sqlService;

import javax.annotation.PostConstruct;

/**
 * SqlReader와 SqlRegistry를 사용하는 SqlService 구현 클래스
 */
public class BaseSqlService implements SqlService {
	// 의존 오브젝트를 DI 받을 수 있도록 인터페이스 타입의 프로퍼티를 선언해둔다.
	// BaseSqlService는 상속을 통해 확장해서 사용하기에 적합하므로 서브클래스에서 필요한 경우 접근할 수 있도록 protected로 선언한다.
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
