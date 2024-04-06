package com.gaga.springtoby.user.sqlService;

/**
 * SqlService 인터페이스
 */
public interface SqlService {
	String getSql(String key) throws SqlRetrievalFailureException;
}
