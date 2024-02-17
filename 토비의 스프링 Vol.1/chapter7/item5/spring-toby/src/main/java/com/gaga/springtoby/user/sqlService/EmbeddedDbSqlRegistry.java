package com.gaga.springtoby.user.sqlService;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 내장형 DB를 사용하는 SQL 레지스트리
 */
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
	JdbcTemplate jdbc;
	// JdbcTemplate와 트랜잭션을 동기화해주는 트랜잭션 템플릿이다. 멀티 스레드 환경에서 공유 가능하다.
	TransactionTemplate transactionTemplate;

	// DataSource를 DI 받아서 JdbcTemplate 형태로 저장해두고 사용한다.
	public void setDataSource(DataSource dataSource) {
		jdbc = new JdbcTemplate(dataSource);
		// dataSource로 TransactionManager를 만들고 이를 이용해 TransactionTemplate을 생성한다.
		transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
	}

	public void registerSql(String key, String sql) {
		jdbc.update("insert into sqlmap(key_, sql_) values(?, ?)", key, sql);
	}

	public String findSql(String key) throws SqlNotFoundException {
		try {
			return jdbc.queryForObject("select sql_ from sqlmap where key_ = ?", String.class, key);
		} catch (EmptyResultDataAccessException e) {
			// 쿼리의 결과가 없으면 예외가 발생한다.
			throw new SqlNotFoundException("에 해당하는 SQL을 찾을 수 없습니다", e);
		}
	}

	public void updateSql(String key, String sql) throws SqlUpdateFailureException {
		// SQL 실행 결과로 영향을 받은 레코드의 개수를 리턴하므로 이를 이용해 주어진 키를 가진 SQL이 존재했는지 확인한다.
		int affected = jdbc.update("update sqlmap set sql_ = ? where key_ = ?", sql, key);
		if (affected == 0) {
			throw new SqlUpdateFailureException("에 해당하는 SQL을 찾을 수 없습니다");
		}
	}

	// 익명 내부 클래스로 만들어지는 콜백 오브젝트 안에서 사용되는 것이므로 final로 선언해줘야 한다.
	public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
		// 트랜잭션 템플릿이 만드는 트랜잭션 경계 안에서 동작할 코드를 콜백 형태로 만들고 TransactionTemplate의 execute() 메소드에 전달한다.
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
					updateSql(entry.getKey(), entry.getValue());
				}
			}
		});
	}
}
