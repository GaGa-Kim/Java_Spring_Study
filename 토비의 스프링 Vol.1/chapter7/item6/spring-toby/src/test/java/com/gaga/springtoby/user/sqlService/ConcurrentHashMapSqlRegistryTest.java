package com.gaga.springtoby.user.sqlService;

/**
 * ConcurrentHashMap을 이용한 SQL 레지스트리 테스트
 */
public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
	@Override
	protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
		return new ConcurrentHashMapSqlRegistry();
	}
}
