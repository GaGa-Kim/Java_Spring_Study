package com.gaga.springtoby.user.sqlService;

import javax.annotation.PostConstruct;

/**
 * 생성자를 통한 디폴트 의존관계 설정
 */
public class DefaultSqlService extends BaseSqlService {
	public DefaultSqlService() {
		// 생성자에서 디폴트 의존 오브젝트를 직접 만들어서 스스로 DI 해준다.
		setSqlReader(new JaxbXmlSqlReader());
		setSqlRegistry(new HashMapSqlRegistry());
	}

	// 의존 오브젝트를 DI 받을 수 있도록 인터페이스 타입의 프로퍼티를 선언해둔다.
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;

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
