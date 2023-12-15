package com.gaga.springtoby.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;

/**
 * UserService 클래스
 */
public class UserServiceImpl implements UserService {

	public static final int MIN_LOGIN_FOR_SILVER = 50;
	public static final int MIN_RECOMMEND_FOR_GOLD = 30;

	UserDao userDao;
	private MailSender mailSender;

	// UserDao 인터페이스 타입으로 userDao 빈을 DI 받아 사용한다.
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/* UserServiceTx로 분리
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	} */

	// 메일 전송 기능을 가진 오브젝트를 DI 받아 사용한다.
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	// 사용자 신규 등록 로직
	@Override
	public void add(User user) {
		if (user.getLevel() == null)
			user.setLevel(Level.BASIC);
		userDao.add(user);
	}

	// 사용자 레벨 업그레이드 메소드
	/* 스프링의 트랜잭션 추상화 API를 적용 -> UserServiceTx로 분리
	public void upgradeLevels() {
		// 트랜잭션 매니저를 빈으로 분리시킨 후 DI 받아 트랜잭션 시작
		// DI 받은 트랜잭션 매니저를 공유해서 사용하므로 멀티스레드 환경에서도 안전한다.
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		// 트랜잭션 안에서 진행되는 작업
		try {
			upgradeLevelsInternal();
			// 정상적으로 작업을 마치면 트랜잭션 커밋
			transactionManager.commit(status);
		} catch (RuntimeException e) {
			// 예외가 발생하면 롤백
			transactionManager.rollback(status);
			throw e;
		}
	} */

	// 사용자 레벨 업그레이드 메소드
	@Override
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for (User user : users) {
			if (canUpgradeLevel(user)) {
				upgradeLevel(user);
			}
		}
	}

	// 업그레이드 가능 확인 메소드
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		// 레벨별로 구분해서 조건을 판단한다.
		switch (currentLevel) {
			case BASIC:
				return (user.getLogin() >= MIN_LOGIN_FOR_SILVER);
			case SILVER:
				return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
			case GOLD:
				return false;
			// 현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다.
			// 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 나서 확인할 수 있다.
			default:
				throw new IllegalArgumentException("Unknown level: " + currentLevel);
		}
	}

	// 레벨 업그레이드
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEMail(user);
	}

	// 스프링의 MailSender를 이용한 메일 발송 메소드
	private void sendUpgradeEMail(User user) {
		// MailMessage 인터페이스의 구현 클래스 오브젝트를 만들어 메일 내용을 작성한다.
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("useradmin@ksug.org");
		mailMessage.setSubject("Upgrade 안내");
		mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

		this.mailSender.send(mailMessage);
	}
}
