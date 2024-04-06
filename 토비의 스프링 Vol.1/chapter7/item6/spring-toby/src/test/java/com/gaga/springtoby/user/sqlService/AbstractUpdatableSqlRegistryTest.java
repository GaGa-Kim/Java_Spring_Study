package com.gaga.springtoby.user.sqlService;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * UpdatableSqlRegistry에 대한 테스트 추상 클래스
 */
public abstract class AbstractUpdatableSqlRegistryTest {
	UpdatableSqlRegistry sqlRegistry;

	@Before
	public void setUp() {
		sqlRegistry = createUpdatableSqlRegistry();
		// 각 테스트 메소드에서 사용할 초기 SQL 정보를 미리 등록해둔다.
		sqlRegistry.registerSql("KEY1", "SQL1");
		sqlRegistry.registerSql("KEY2", "SQL2");
		sqlRegistry.registerSql("KEY3", "SQL3");
	}

	// 테스트 픽스처를 생성하는 부분만 추상 메소드로 만들어 두고 서브 클래스에서 이를 구현하도록 만든다.
	abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

	@Test
	public void find() {
		checkFindResult("SQL1", "SQL2", "SQL3");
	}

	// 주어진 키에 해당하는 SQL을 찾을 수 없을 때 예외가 발생하는지 확인한다.
	@Test(expected = SqlNotFoundException.class)
	public void unknownKey() {
		sqlRegistry.findSql("SQL9999!@#$");
	}

	// 하나의 SQL을 변경하는 기능에 대한 테스트다.
	@Test
	public void updateSingle() {
		sqlRegistry.updateSql("KEY2", "Modified2");
		checkFindResult("SQL1", "Modified2", "SQL3");
	}

	// 한 번에 여러 개의 SQL을 수정하는 기능을 검증한다.
	@Test
	public void updateMulti() {
		Map<String, String> sqlmap = new HashMap<String, String>();
		sqlmap.put("KEY1", "Modified1");
		sqlmap.put("KEY3", "Modified3");

		sqlRegistry.updateSql(sqlmap);
		checkFindResult("Modified1", "SQL2", "Modified3");
	}

	// 존재하지 않는 키의 SQL을 변경하려고 시도할 때 예외가 발생하는 것을 검증한다.
	@Test(expected = SqlUpdateFailureException.class)
	public void updateWithNotExistingKey() {
		sqlRegistry.updateSql("SQL9999!@#$", "Modified2");
	}

	// 반복적으로 검증하는 부분은 별도의 메소드로 분리해두면 테스트 코드가 깔끔해진다.
	// 서브 클래스에서 테스트를 추가한다면 필요로 할 수 있으므로 저급 가능하도록 protected로 변경한다.
	protected void checkFindResult(String expected1, String expected2, String expected3) {
		assertThat(sqlRegistry.findSql("KEY1"), is(expected1));
		assertThat(sqlRegistry.findSql("KEY2"), is(expected2));
		assertThat(sqlRegistry.findSql("KEY3"), is(expected3));
	}
}
