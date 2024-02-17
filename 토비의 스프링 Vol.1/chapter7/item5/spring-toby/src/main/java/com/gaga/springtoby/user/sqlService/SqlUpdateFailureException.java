package com.gaga.springtoby.user.sqlService;

/**
 * SQL 수정 실패 시 예외
 */
public class SqlUpdateFailureException extends RuntimeException { // 복구 불가능한 런타임 예외로 정의
	public SqlUpdateFailureException(String message) {
		super(message);
	}

	// SQL을 수정하는데 실패한 근본 원인을 담을 수 있도록 중첩 예외를 저장할 수 있는 생성자를 만들어둔다.
	public SqlUpdateFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}