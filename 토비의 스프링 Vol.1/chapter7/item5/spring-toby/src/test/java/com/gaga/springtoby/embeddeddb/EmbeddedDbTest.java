package com.gaga.springtoby.embeddeddb;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

/**
 * 내장형 DB 학습 테스트
 */
public class EmbeddedDbTest {
	EmbeddedDatabase db;
	JdbcTemplate template;

	@Before
	public void setUp() {
		db = new EmbeddedDatabaseBuilder()
			.setType(HSQL) // EmbeddedDatabaseType의 HSQL, DERBY, H2 중에서 하나를 선택한다.
			.addScript("classpath:schema.sql") // 초기화에 사용할 DB 스크립트의 리소스 (테이블 생성)
			.addScript("classpath:data.sql") // 초기화에 사용할 DB 스크립트의 리소스 (데이터 초기화)
			.build(); // 주어진 조건에 맞는 내장형 DB를 준비하고 초기화 스크립트를 모두 실행한 뒤에 이에 접근할 수 있는 EmbeddedDatabase를 돌려준다.

		// EmbeddedDatabase는 DataSource의 서브 인터페이스이므로 DataSource를 필요로 하는 JdbcTemplate를 만들 때 사용할 수 있다.
		template = new JdbcTemplate(db);
	}

	@After
	public void tearDown() {
		// 매 테스트를 진행한 뒤에 DB를 종료한다.
		// 내장형 메모리 DB는 따로 저장하지 않는 한 애플리케이션과 함께 매번 새롭게 만들어지고 제거되는 생명주기를 갖는다.
		db.shutdown();
	}

	// 초기화 스크립트를 통해 등록된 데이터를 검증하는 테스트다.
	@Test
	public void initData() {
		assertThat(template.queryForObject("select count(*) from sqlmap", Integer.class), is(2));

		List<Map<String, Object>> list = template.queryForList("select * from sqlmap order by key_");
		assertThat((String)list.get(0).get("key_"), is("KEY1"));
		assertThat((String)list.get(0).get("sql_"), is("SQL1"));
		assertThat((String)list.get(1).get("key_"), is("KEY2"));
		assertThat((String)list.get(1).get("sql_"), is("SQL2"));
	}

	// 새로운 데이터를 추가하고 이를 확인해본다.
	@Test
	public void insert() {
		template.update("insert into sqlmap(key_, sql_) values(?, ?)", "KEY3", "SQL3");

		assertThat(template.queryForObject("select count(*) from sqlmap", Integer.class), is(3));
	}
}
