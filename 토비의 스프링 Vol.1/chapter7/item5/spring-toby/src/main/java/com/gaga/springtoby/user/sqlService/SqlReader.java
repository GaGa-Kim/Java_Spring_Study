package com.gaga.springtoby.user.sqlService;

/**
 * SqlReader 인터페이스
 */
public interface SqlReader {
	// SQL을 외부에서 가져와 SqlRegistry에 등록한다.
	// 다양한 예외가 발생할 수 있겠지만 대부분 복구 불가능한 예외이므로 굳이 예외를 선헌해두지 않았다.
	void read(SqlRegistry sqlRegistry);
}
