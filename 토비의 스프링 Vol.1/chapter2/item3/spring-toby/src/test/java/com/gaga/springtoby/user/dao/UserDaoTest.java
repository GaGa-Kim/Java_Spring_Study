package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * JUnit을 적용한 UserDaoTest
 */
public class UserDaoTest {

    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    // 스프링의 애플리케이션 컨텍스트를 만드는 부분과 컨텍스트에서 UserDao를 가져오는 부분을 별도의 메소드로 꺼낸다.
    @Before // 매번 테스트 메소드를 실행하기 전에 먼저 실행시켜준다.
    public void setUp() {

        // XML 애플리케이션 컨텍스트로 UserDao 오브젝트를 받아온다.
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        this.userDao = context.getBean("userDao", UserDao.class);

        // 3개의 사용자 정보를 하나씩 추가한다.
        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("bumjin", "박범진", "springno3");
    }

    // JUnit에게 테스트용 메소드임을 알려준다.
    @Test
    // JUnit 테스트 메소드는 반드시 public으로 선언돼야 한다.
    public void addAndGet() throws SQLException {

        // deleteAll() 메소드를 이용해 DB의 모든 내용을 삭제한다.
        userDao.deleteAll();

        // 레코드의 개수가 0인지 확인한다.
        assertThat(userDao.getCount(), is(0));

        // add() 메소드를 이용해 DB에 등록해본다.
        userDao.add(user1);
        userDao.add(user2);

        // 첫 번째 User의 id로 get()을 실행하면 첫 번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
        User userget1 = userDao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        // 두 번째 User의 id로 get()을 실행하면 두 번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
        User userget2 = userDao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    // 레코드를 add() 했을 때 getCount()에 대한 좀 더 꼼꼼한 테스트
    @Test
    public void count() throws SQLException {

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
    public void getUserFailure() throws SQLException {

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        // 존재하지 않는 id로 get()을 호출한다.
        userDao.get("unknown_id"); // 예외가 발생한다.
    }
}