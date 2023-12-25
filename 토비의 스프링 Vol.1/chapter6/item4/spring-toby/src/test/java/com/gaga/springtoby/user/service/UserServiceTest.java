package com.gaga.springtoby.user.service;

import static com.gaga.springtoby.user.service.UserServiceImpl.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
	UserService userService; // 빈 아이디가 userService인 UserServiceTx가 빈으로 주입

	@Autowired
	UserServiceImpl userServiceImpl;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	private UserDao userDao;

	@Autowired
	MailSender mailSender;

	@Autowired // 팩토리 빈을 가져오려면 애플리케이션 컨텍스트가 필요하다.
	ApplicationContext context;

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
		assertThat(this.userServiceImpl, is(notNullValue()));
	}

	// getAll()에 대해서는 스텁으로서, update()에 대해서는 목 오브젝트로서 동작하는 UserDao 타입의 테스트 대역
	static class MockUserDao implements UserDao {
		// 레벨 업그레이드 후보 User 오브젝트 목록
		private List<User> users;
		// 업그레이드 대상 오브젝트를 저장해둘 목록
		private List<User> updated = new ArrayList<>();

		// 생성자를 통해 전달받은 사용자 목록을 저장해뒀다가 getAll() 메소드가 호출되면 DB에서 가져온 것처럼 돌려주는 용도
		private MockUserDao(List<User> users) {
			this.users = users;
		}

		// update() 메소드를 실행하면서 넘겨준 업그레이드 대상 User 오브젝트를 저장해뒀다가 검증을 위해 돌려줌
		public List<User> getUpdated() {
			return this.updated;
		}

		// 스텁 기능 제공
		// 테스트용 UserDao에는 DB에서 읽어온 것처럼 미리 준비된 사용자 목록을 제공
		public List<User> getAll() {
			return this.users;
		}

		// 목 오브젝트 기능 제공
		// 업그레이드를 통해 레벨이 변경된 사용자는 DB에 반영되도록 userDao의 update()에 전달할 사용자 목록을 제공
		public void update(User user) {
			updated.add(user);
		}

		// 테스트에 사용되지 않는 메소드
		public void add(User user) {
			throw new UnsupportedOperationException();
		}

		public void deleteAll() {
			throw new UnsupportedOperationException();
		}

		public User get(String id) {
			throw new UnsupportedOperationException();
		}

		public int getCount() {
			throw new UnsupportedOperationException();
		}
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
		// (1) DB 테스트 데이터 준비 -> 목 오브젝트(MockUserDao)를 만들어서 적용
		// 고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		// 목 오브젝트로 만든 UserDao를 직접 DI
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		// (2) 메일 발송 여부 확인을 위해 목 오브젝트 DI
		// 메일 발송 결과를 테스트할 수 있도록 목 오브젝트를 만들어 userService의 의존 오브젝트로 주입해준다.
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);

		// (3) 테스트 대상 실행
		// 업그레이드 테스트, 메일 발송이 일어나면 MockMailSender 오브젝트의 리스트에 그 결과가 저장된다.
		userServiceImpl.upgradeLevels(); // 이제 UserService (x) UserServiceImpl (o)

		// (4) DB에 저장된 결과 확인 -> 목 오브젝트(MockUserDao)를 만들어서 적용
		// 각 사용자별로 업그레이드 후의 예상 레벨을 검증한다.
		// MockUserDao로부터 업데이트를 결과를 가져온다.
		List<User> updated = mockUserDao.getUpdated();
		// 업데이트 횟수와 정보를 확인한다.
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

		// (5) 목 오브젝트를 이용한 결과 확인
		// 목 오브젝트에 저장된 메일 수신자 목록을 가져와 업그레이드 대상과 일치하는지 확인한다.
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}

	// id와 level을 확인하는 간단한 헬퍼 메소드
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

	// Mockito를 적용한 테스트 코드
	@Test
	public void mockUpgradeLevels() throws Exception {
		UserServiceImpl userServiceImpl = new UserServiceImpl();

		// 다이내믹한 목 오브젝트 생성과 메소드의 리턴 값 설정, 그리고 DI까지 세 줄이면 충분하다.
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);

		// 리턴 값이 없는 메소드를 가진 목 오브젝트는 더욱 간단하게 만들 수 있다.
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);

		// 목 오브젝트의 준비와 DI를 마친 userServiceImpl 오브젝트는 이제 고립된 테스트 가능
		userServiceImpl.upgradeLevels();

		// 목 오브젝트가 제공하는 검증 기능을 통해서 어떤 메소드가 몇 번 호출됐는지, 파라미터는 무엇인지 확인할 수 있다.
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));

		// 파라미터를 정밀하게 검사하기 위해 캡처할 수도 있다.
		// 실제 MailSender 목 오브젝트에 전달된 파라미터를 가져와 내용을 검증한다.
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
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
	static class TestUserService extends UserServiceImpl {
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
	@DirtiesContext // 다이내믹 프록시 팩토리 빈을 직접 만들어 사용할 때는 없앴다가 다시 등장한 컨텍스트 무효화 애노테이션
	public void upgradeAllOrNothing() throws Exception {
		// 예외를 발생시킬 네 번째 사용자의 id를 넣어서 테스트용 UserService 대역 오브젝트를 생성한다.
		TestUserService testUserService = new TestUserService(users.get(3).getId());
		// userDao를 수동 DI 해준다.
		testUserService.setUserDao(this.userDao);
		// 테스트용 UserService를 위한 메일 전송 오브젝트 빈인 MailSender를 수동 DI 해준다.
		testUserService.setMailSender(this.mailSender);

		/* 트랜잭션 프록시 팩토리 빈을 적용하여 트랜잭션을 테스트한다.
		// 팩토리 빈 자체를 가져와야 하므로 빈 이름에 &를 반드시 넣어야 한다.
		TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class); // 테스트용 타깃 주입
		txProxyFactoryBean.setTarget(testUserService);
		// 변경된 타깃 설정을 이용해서 트랜잭션 다이내믹 프록시 오브젝트를 다시 생성한다.
		UserService txUserService = (UserService)txProxyFactoryBean.getObject(); */

		// ProxyFactoryBean을 이용하여 트랜잭션을 테스트한다.
		// 팩토리 빈 자체를 가져와야 하므로 빈 이름에 &를 반드시 넣어야 한다.
		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
		txProxyFactoryBean.setTarget(testUserService);
		// 변경된 타깃 설정을 이용해서 트랜잭션 다이내믹 프록시 오브젝트를 다시 생성한다.
		UserService txUserService = (UserService)txProxyFactoryBean.getObject();

		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		// TestUserService는 업그레이드 작업 중에 예외가 발생해야 한다.
		try {
			// 트랜잭션 기능을 분리한 오브젝트를 통해 예외 발생용 TestUserService가 호출되게 해야 한다.
			txUserService.upgradeLevels(); // UserService (TxProxyFactoryBean) -> UserServiceImpl
			fail("TestUserServiceException expected");
		}
		// TestUserService가 던져주는 예외를 잡아서 계속 진행되도록 한다.
		catch (TestUserServiceException e) {
		}

		// 예외가 발생하기 전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 확인한다.
		checkLevelUpgraded(users.get(1), false);
	}
}
