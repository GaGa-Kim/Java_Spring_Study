package com.gaga.springtoby.user.service;

import static com.gaga.springtoby.user.service.UserService.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;

/**
 * UserServiceTest 클래스
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	private UserDao userDao;

	@Autowired
	MailSender mailSender;

	List<User> users; // 테스트 픽스처

	@Before
	public void setUp() {
		users = Arrays.asList(
			new User("bumjin", "bumjin@email,com", "박범진", "p1", Level.BASIC, MIN_LOGIN_FOR_SILVER - 1, 0),
			new User("joytouch", "joytouch@email,com", "강명성", "p2", Level.BASIC, MIN_LOGIN_FOR_SILVER, 0),
			new User("erwins", "erwins@email,com", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
			new User("madnite1", "madnite1@email,com", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
			new User("green", "green@email,com", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
		);
	}

	// userService 빈의 주입을 확인하는 테스트
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}

	// 목 오브젝트로 만든 메일 전송 확인용 MailSender 구현 클래스
	static class MockMailSender implements MailSender {
		// UserService로부터 전송 요청을 받은 메일 주소를 저장해두고 이를 읽을 수 있게 한다.
		private List<String> requests = new ArrayList<String>();

		public List<String> getRequests() {
			return requests;
		}

		public void send(SimpleMailMessage mailMessage) throws MailException {
			// 전송 요청을 받은 이메일 주소를 저장해준다.
			// 간단하게 첫 번째 수신자 메일 주소만 저장했다.
			requests.add(mailMessage.getTo()[0]);
		}

		public void send(SimpleMailMessage[] mailMessage) throws MailException {
		}
	}

	// 사용자 레벨 업그레이드 테스트이자 메일 발송 대상을 확인하는 테스트
	@Test
	@DirtiesContext // 컨텍스트의 DI 설정을 변경(DummyMailSender -> MockMailSender)하는 테스트라는 것을 알려준다.
	public void upgradeLevels() throws Exception {
		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		// 메일 발송 결과를 테스트할 수 있도록 목 오브젝트를 만들어 userService의 의존 오브젝트로 주입해준다.
		MockMailSender mockMailSender = new MockMailSender();
		userService.setMailSender(mockMailSender);

		// 업그레이드 테스트, 메일 발송이 일어나면 MockMailSender 오브젝트의 리스트에 그 결과가 저장된다.
		userService.upgradeLevels();

		// 각 사용자별로 업그레이드 후의 예상 레벨을 검증한다.
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);

		// 목 오브젝트에 저장된 메일 수신자 목록을 가져와 업그레이드 대상과 일치하는지 확인한다.
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}

	// add() 메소드의 테스트
	@Test
	public void add() {
		userDao.deleteAll();

		// GOLD 레벨이 이미 지정된 User라면 레벨을 초기화하지 않아야 한다.
		User userWithLevel = users.get(4);
		// 레벨이 비어 있는 사용자로 수정한다.
		// 로직에 따라 등록 중에 BASIC 레벨이 설정되어야 한다.
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);

		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		// DB에 저장된 결과를 가져와 확인한다.
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}

	// DB에서 사용자 정보를 가져와 레벨을 확인하는 코드를 헬퍼 메소드로 분리한다.
	// 어떤 레벨로 바뀔 것인가가 아니라, 다음 레벨로 업그레이드할 것인가 아닌가를 지정하도록 개선한다.
	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if (upgraded) {
			// 업그레이드가 일어났는지 확인한다.
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		} else {
			// 업그레이드가 일어나지 않았는지 확인한다.
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}

	// UserService의 테스트용 대역 클래스
	static class TestUserService extends UserService {
		private String id;

		// 예외를 발생시킬 User 오브젝트의 id를 지정할 수 있게 만든다.
		private TestUserService(String id) {
			this.id = id;
		}

		// UserService의 메소드를 오버라이딩한다.
		protected void upgradeLevel(User user) {
			// 지정된 id의 User 오브젝트가 발견되면 예외를 던져서 작업을 강제로 중단시킨다.
			if (user.getId().equals(this.id))
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}

	// 테스트용 예외
	static class TestUserServiceException extends RuntimeException {
	}

	// 예외 발생 시 작업 취소 여부 테스트
	@Test
	public void upgradeAllOrNothing() throws Exception {
		// 예외를 발생시킬 네 번째 사용자의 id를 넣어서 테스트용 UserService 대역 오브젝트를 생성한다.
		UserService testUserService = new TestUserService(users.get(3).getId());
		// userDao를 수동 DI 해준다.
		testUserService.setUserDao(this.userDao);
		// 트랜잭션 매니저 빈인 PlatformTransactionManager를 수동 DI 해준다.
		testUserService.setTransactionManager(this.transactionManager);
		// 테스트용 UserService를 위한 메일 전송 오브젝트 빈인 MailSender를 수동 DI 해준다.
		testUserService.setMailSender(this.mailSender);

		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		// TestUserService는 업그레이드 작업 중에 예외가 발생해야 한다.
		try {
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}
		// TestUserService가 던져주는 예외를 잡아서 계속 진행되도록 한다.
		catch (TestUserServiceException e) {
		}

		// 예외가 발생하기 전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 확인한다.
		checkLevelUpgraded(users.get(1), false);

		// 메일 전송을 확인한다.
		testUserService.setMailSender(mailSender);
	}
}
