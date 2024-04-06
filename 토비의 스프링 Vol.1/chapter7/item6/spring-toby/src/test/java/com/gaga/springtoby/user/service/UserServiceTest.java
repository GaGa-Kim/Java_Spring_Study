package com.gaga.springtoby.user.service;

import static com.gaga.springtoby.user.service.UserServiceImpl.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gaga.springtoby.AppContext;
import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;

/**
 * UserServiceTest 클래스
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "/applicationContext.xml")
// @ContextConfiguration(classes = {TestAppContext.class, AppContext.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = AppContext.class)
public class UserServiceTest {

	@Autowired
	UserService userService; // 빈 아이디가 userService인 빈이 주입

	@Autowired
	UserService testUserService; // 같은 타입의 빈이 두 개 존재하기 때문에 필드 이름을 기준으로 주입될 빈이 결정

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

	// 자동생성된 프록시 확인
	@Test
	public void advisorAutoProxyCreator() {
		// 프록시로 변경된 오브젝트인지 확인한다.
		assertThat(testUserService, instanceOf(java.lang.reflect.Proxy.class));
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
	// 메소드에서 디폴트 설정과 그 밖의 롤백 방법으로 재설정할 수 있다.
	@Rollback
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

	// 패키지 문제로 인해, TestUserSerivce 클래스를 생성하여 이동
	/* 수정한 테스트용 UserService 구현 클래스
	private static class TestUserService extends UserServiceImpl { // 포인트컷의 클래스 필터에 선정되도록 이름 변경
		// 예외를 발생시킬 User 오브젝트의 id를 지정한다.
		private String id = "madnite1"; // 테스트 픽스처의 users(3)의 id 값을 고정시켜버렸다.

		// UserService의 메소드를 오버라이딩한다.
		protected void upgradeLevel(User user) {
			// 지정된 id의 User 오브젝트가 발견되면 예외를 던져서 작업을 강제로 중단시킨다.
			if (user.getId().equals(this.id))
				throw new TestUserServiceException();
			super.upgradeLevel(user);
		}

		// 읽기전용 트랜잭션 대상인 get으로 시작하는 메소드를 오버라이드한다.
		public List<User> getAll() {
			for (User user : super.getAll()) {
				// 강제로 쓰기 시도를 하여 읽기전용 속성으로 인한 예외가 발생해야 한다.
				super.update(user);
			}
			// 메소드가 끝나기 전에 예외가 발생해야 하니 리턴 값은 별 의미 없으므로 적당한 값을 넣어서 컴파일만 되게 한다.
			return null;
		}
	}

	// 테스트용 예외
	static class TestUserServiceException extends RuntimeException {
	}
	*/

	// 읽기전용 속성 테스트
	@Test(expected = TransientDataAccessException.class)
	public void readOnlyTransactionAttribute() {
		// 트랜잭션 속성이 제대로 적용됐다면 여기서 읽기전용 속성을 위반했기 때문에 예외가 발생해야 한다.
		testUserService.getAll();
	}

	// 예외 발생 시 작업 취소 여부 테스트
	@Test
	public void upgradeAllOrNothing() {
		userDao.deleteAll();
		for (User user : users)
			userDao.add(user);

		// TestUserService는 업그레이드 작업 중에 예외가 발생해야 한다.
		try {
			// 트랜잭션 기능을 분리한 오브젝트를 통해 예외 발생용 TestUserService가 호출되게 해야 한다.
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}
		// TestUserService가 던져주는 예외를 잡아서 계속 진행되도록 한다.
		catch (TestUserService.TestUserServiceException e) {
		}

		// 예외가 발생하기 전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 확인한다.
		checkLevelUpgraded(users.get(1), false);
	}

	// 트랜잭션 매니저를 참조하는 테스트
	@Test
	public void transactionSync() {
		// 트랜잭션 매니저를 이용해 트랜잭션을 미리 시작하게 한다.
		// 트랜잭션 정의는 기본 값을 사용한다.
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		// 읽기전용 트랜잭션으로 정의한다.
		// txDefinition.setReadOnly(true);
		// 트랜잭션 매니저에게 트랜잭션을 요청한다.
		// 기존에 시작된 트랜잭션이 없으니 새로운 트랜잭션을 시작시키고 트랜잭션 정보를 돌려준다.
		// 동시에 만들어진 트랜잭션을 다른 곳에서도 사용할 수 있도록 동기화한다.
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

		// 앞에서 만들어진 트랜잭션에 모두 참여한다.
		userService.deleteAll();

		userService.add(users.get(0));
		userService.add(users.get(1));

		// 앞에서 시작한 트랜잭션을 커밋한다.
		transactionManager.commit(txStatus);
	}

	// 트랜잭션 롤백 테스트
	@Test
	public void transactionRollback() {
		// 트랜잭션을 롤백했을 때 돌아갈 초기 상태를 만들기 위해 트랜잭션 시작 전에 초기화를 해둔다.
		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

		userService.add(users.get(0));
		userService.add(users.get(1));
		// userDao의 getCount() 메소드도 같은 트랜잭션에서 동작한다.
		assertThat(userDao.getCount(), is(2));

		// 강제로 롤백한다. 트랜잭션 시작 전 상태로 돌아가야 한다.
		transactionManager.rollback(txStatus);

		// add()의 작업이 취소되고 트랜잭션 시작 이전의 상태임을 확인할 수 있다.
		assertThat(userDao.getCount(), is(0));
	}

	// 롤백 테스트
	@Test
	public void rollbackTest() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

		try {
			// 테스트 안의 모든 작업을 하나의 트랜잭션으로 통합한다.
			userService.deleteAll();
			userService.add(users.get(0));
			userService.add(users.get(1));
		} finally {
			// 테스트 결과가 어떻든 상관없이 테스트가 끝나면 무조건 롤백한다.
			// 테스트 중에 발생했던 DB의 변경사항은 모두 이전 상태로 복구된다.
			transactionManager.rollback(txStatus);
		}
	}

	// 트랜잭션 애노테이션 테스트
	@Test
	@Transactional
	// @Transactional(readOnly = true)
	@Rollback(value = false)
	public void transactionAnnotation() {
		userService.deleteAll();
		userService.add(users.get(0));
		userService.add(users.get(1));
	}

	// 등록된 빈 내역을 조회하는 테스트 메소드
	@Autowired
	DefaultListableBeanFactory bf;

	@Test
	public void beans() {
		for (String n : bf.getBeanDefinitionNames()) {
			System.out.println(n + "\t " + bf.getBean(n).getClass().getName());
		}
	}
}
