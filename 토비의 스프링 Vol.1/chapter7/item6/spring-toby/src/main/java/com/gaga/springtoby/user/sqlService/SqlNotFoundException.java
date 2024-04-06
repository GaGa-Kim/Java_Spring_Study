package com.gaga.springtoby.user.sqlService;

/**
 * SQL 검색 실패 시 예외
 */
public class SqlNotFoundException extends RuntimeException {
	public SqlNotFoundException() {
		super();
	}

	public SqlNotFoundException(String message) {
		super(message);
	}

	public SqlNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}