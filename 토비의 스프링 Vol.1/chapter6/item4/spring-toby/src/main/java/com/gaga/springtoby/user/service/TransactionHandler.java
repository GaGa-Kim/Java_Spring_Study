package com.gaga.springtoby.user.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 다이내믹 프록시를 위한 트랜잭션 부가기능
 */
public class TransactionHandler implements InvocationHandler {
	// 부가기능을 제공할 타깃 오브젝트, 어던 타입의 오브젝트에도 적용 가능하다.
	private Object target;
	// 트랜잭션 기능을 제공하는데 필요한 트랜잭션 매니저
	private PlatformTransactionManager transactionManager;
	// 트랜잭션을 적용한 메소드 이름 패턴
	private String pattern;

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 트랜잭션 적용 대상 메소드를 선별해서 트랜잭션 경계설정 기능을 부여해준다.
		if (method.getName().startsWith(pattern)) {
			return invokeTransaction(method, args);
		} else {
			return method.invoke(target, args);
		}
	}

	private Object invokeTransaction(Method method, Object[] args) throws Throwable {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			// 트랜잭션을 시작하고 타깃 오브젝트의 메소드를 호출한다.
			// 예외가 발생하지 않았다면 커밋한다.
			Object ret = method.invoke(target, args);
			this.transactionManager.commit(status);
			return ret;
		}
		// 예외가 발생하면 트랜잭션을 롤백한다.
		catch (InvocationTargetException e) {
			this.transactionManager.rollback(status);
			throw e.getTargetException();
		}
	}
}
