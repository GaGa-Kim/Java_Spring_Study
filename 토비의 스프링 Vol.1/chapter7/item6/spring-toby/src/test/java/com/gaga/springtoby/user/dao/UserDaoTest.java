package com.gaga.springtoby.user.dao;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gaga.springtoby.AppContext;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;

/**
 * 스프링 테스트 컨텍스트를 적용한 UserDaoTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "/applicationContext.xml")
// @ContextConfiguration(classes = {TestAppContext.class, AppContext.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = AppContext.class)
public class UserDaoTest {

	// UserDao 인터페이스
	@Autowired
	private UserDao userDao;

	private User user1;
	private User user2;
	private User user3;

	@Before // 매번 테스트 메소드를 실행하기 전에 먼저 실행시켜준다.
	public void setUp() {

		// 3개의 사용자 정보를 하나씩 추가한다.
		this.user1 = new User("gyumee", "springno1@email.com", "박성철", "springno1", Level.BASIC, 1, 0);
		this.user2 = new User("leegw700", "springno2@email.com", "이길원", "springno2", Level.SILVER, 55, 10);
		this.user3 = new User("bumjin", "springno3@email.com", "박범진", "springno3", Level.GOLD, 100, 40);
	}

	@Test
	public void addAndGet() {

		// deleteAll() 메소드를 이용해 DB의 모든 내용을 삭제한다.
		userDao.deleteAll();

		// 레코드의 개수가 0인지 확인한다.
		assertThat(userDao.getCount(), is(0));

		// add() 메소드를 이용해 DB에 등록해본다.
		userDao.add(user1);
		userDao.add(user2);

		// 첫 번째 User의 id로 get()을 실행하면 첫 번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
		User userget1 = userDao.get(user1.getId());
		checkSameUser(userget1, user1);

		// 두 번째 User의 id로 get()을 실행하면 두 번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
		User userget2 = userDao.get(user2.getId());
		checkSameUser(userget2, user2);
	}

	// 레코드를 add() 했을 때 getCount()에 대한 좀 더 꼼꼼한 테스트
	@Test
	public void count() {

		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		// 3개의 사용자 정보를 하나씩 추가하면서 매번 getCount()의 결과가 하나씩 증가하는지 확인한다.
		userDao.add(user1);
		assertThat(userDao.getCount(), is(1));

		userDao.add(user2);
		assertThat(userDao.getCount(), is(2));

		userDao.add(user3);
		assertThat(userDao.getCount(), is(3));
	}

	// 사용자 정보가 없을 때 get() 테스트
	@Test(expected = EmptyResultDataAccessException.class) // 발생할 것으로 기대되는 예외 클래스를 지정해준다.
	public void getUserFailure() {

		userDao.deleteAll();
		assertThat(userDao.getCount(), is(0));

		// 존재하지 않는 id로 get()을 호출한다.
		userDao.get("unknown_id"); // 예외가 발생한다.
	}

	// 모든 사용자 정보를 가져오는 getAll() 테스트
	@Test
	public void getAll() {

		userDao.deleteAll();

		// 데이터가 없을 때는 크기가 0인 리스트 오브젝트가 리턴돼야 한다.
		List<User> users0 = userDao.getAll();
		assertThat(users0.size(), is(0));

		userDao.add(user1); // id : gyumee
		List<User> users1 = userDao.getAll();
		assertThat(users1.size(), is(1));
		checkSameUser(user1, users1.get(0));

		userDao.add(user2); // id : leegw700
		List<User> users2 = userDao.getAll();
		assertThat(users2.size(), is(2));
		checkSameUser(user1, users2.get(0));
		checkSameUser(user2, users2.get(1));

		userDao.add(user3); // id : bumjin
		List<User> users3 = userDao.getAll();
		assertThat(users3.size(), is(3));
		checkSameUser(user3, users3.get(0)); // user3의 id 값이 알파벳순으로 가장 빠르므로 첫 번째로 가도록 한다.
		checkSameUser(user1, users3.get(1));
		checkSameUser(user2, users3.get(2));
	}

	// DataAccessException에 대한 테스트
	// @Test(expected = DataAccessException.class)
	@Test(expected = DuplicateKeyException.class)
	public void duplicateKey() {

		userDao.deleteAll();

		// 강제로 같은 사용자를 두 번 등록한다.
		userDao.add(user1);
		// 여기서 예외가 발생해야 한다.
		userDao.add(user1);
	}

	// 사용자 정보를 수정하는 update() 테스트
	@Test
	public void update() {
		userDao.deleteAll();

		userDao.add(user1); // 수정할 사용자
		userDao.add(user2); // 수정하지 않을 사용자

		// 픽스처에 들어 있는 정보를 변경해서 수정 메소드를 호출한다.
		user1.setEmail("springno6@email.com");
		user1.setName("오민규");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(1000);
		user1.setRecommend(999);

		userDao.update(user1);

		// 사용자 두 명 중 하나만 수정한 후 원하는 사용자 외의 정보는 변경되지 않았음을 직접 확인한다.
		User user1update = userDao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = userDao.get(user2.getId());
		checkSameUser(user2, user2same);
	}

	// User 오브젝트의 내용을 비교하는 검증 코드에서 반복적으로 사용되므로 분리해놓았다.
	private void checkSameUser(User user1, User user2) {
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getEmail(), is(user2.getEmail()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLevel(), is(user2.getLevel()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
	}
}