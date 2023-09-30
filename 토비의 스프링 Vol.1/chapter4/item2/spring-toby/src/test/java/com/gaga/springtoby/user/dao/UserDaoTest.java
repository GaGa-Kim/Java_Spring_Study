package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 스프링 테스트 컨텍스트를 적용한 UserDaoTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {

    // UserDao 인터페이스
    @Autowired
    private UserDao userDao;

    // 에러 코드 변환에 필요한 DB의 종류를 현재 연결된 DataSource를 사용해 알아낸다.
    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @Before // 매번 테스트 메소드를 실행하기 전에 먼저 실행시켜준다.
    public void setUp() {

        // 3개의 사용자 정보를 하나씩 추가한다.
        this.user1 = new User("gyumee", "박성철", "springno1");
        this.user2 = new User("leegw700", "이길원", "springno2");
        this.user3 = new User("bumjin", "박범진", "springno3");
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
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        // 두 번째 User의 id로 get()을 실행하면 두 번째 User의 값을 가진 오브젝트를 돌려주는지 확인한다.
        User userget2 = userDao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
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
        checkSameUser(user3, users3.get(0)); // user3의 id 값이 알파벳순으로 가장 빠르믈 첫 번째로 가도록 한다.
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

    // SQLException 젼환 기능의 학습 테스트
    // DataSource를 사용해 SQLException에서 직접 DuplicateKeyException으로 전환
    // JDBC에 따라 SQLException이 세분화해서 정의되어 SQLIntegrityConstraintViolationException이 발생할 수 있음
    @Test
    public void sqlExceptionTranslate() {

        userDao.deleteAll();

        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch(DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException) ex.getRootCause();
            // 코드를 이용한 SQLException을 DataAccessException 타입의 예외로 변환
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            // 변환된 DataAccessException 타입의 예외는 DuplicateKeyException 타입임을 확인
            assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
        }
    }

    // User 오브젝트의 내용을 비교하는 검증 코드에서 반복적으로 사용되므로 분리해놓았다.
    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
    }
}