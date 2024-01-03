package com.gaga.springtoby.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * MethodInterceptor 구현 클래스
 */
public class UppercaseAdvice implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있으므로
		// 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다.
		String ret = (String)invocation.proceed();
		return ret.toUpperCase(); // 부가기능 적용
	}
}
