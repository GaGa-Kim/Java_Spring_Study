package com.gaga.springtoby.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gaga.springtoby.user.domain.User;

/**
 * 위임 기능을 가진 UserServiceTx
 */
public class UserServiceTx {
	// UserService를 구현한 다른 오브젝트를 DI 받는다.
	/* 타깃 오브젝트 */
	UserService userService;
	PlatformTransactionManager transactionManager;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	// DI 받은 UserService 오브젝트에 모든 기능을 위임한다.
	/* 메소드 구현과 위임 */
	public void add(User user) {
		userService.add(user);
	}

	public void upgradeLevels() { // 메소드 구현
		// 트랜잭션 매니저를 빈으로 분리시킨 후 DI 받아 트랜잭션 시작
		// DI 받은 트랜잭션 매니저를 공유해서 사용하므로 멀티스레드 환경에서도 안전한다.
		/* 부가기능 수행 */
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		// 트랜잭션 안에서 진행되는 작업
		try {
			/* 위임 */
			userService.upgradeLevels();
			// 정상적으로 작업을 마치면 트랜잭션 커밋
			/* 부가기능 수행 */
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			// 예외가 발생하면 롤백
			transactionManager.rollback(status);
			throw e;
		}
	}
}
