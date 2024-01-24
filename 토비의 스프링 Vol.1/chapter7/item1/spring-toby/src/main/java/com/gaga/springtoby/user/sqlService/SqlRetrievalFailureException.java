package com.gaga.springtoby.user.sqlService;

/**
 * SQL 조회 실패 시 예외
 */
public class SqlRetrievalFailureException extends RuntimeException { // 복구 불가능한 런타임 예외로 정의
	public SqlRetrievalFailureException(String message) {
		super(message);
	}

	// SQL을 가져오는데 실패한 근본 원인을 담을 수 있도록 중첩 예외를 저장할 수 있는 생성자를 만들어둔다.
	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
