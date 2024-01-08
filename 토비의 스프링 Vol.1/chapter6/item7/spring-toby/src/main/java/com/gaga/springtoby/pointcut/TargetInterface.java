package com.gaga.springtoby.pointcut;

/**
 * 포인트컷 테스트용 인터페이스
 */
public interface TargetInterface {
	public void hello();

	public void hello(String a);

	public int minus(int a, int b);

	public int plus(int a, int b);
}
