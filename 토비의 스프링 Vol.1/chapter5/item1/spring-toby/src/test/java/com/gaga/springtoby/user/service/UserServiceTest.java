package com.gaga.springtoby.user.service;

import static com.gaga.springtoby.user.service.UserService.MIN_LOGIN_FOR_SILVER;
import static com.gaga.springtoby.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * UserServiceTest 클래스
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    private UserDao userDao;

    List<User> users; // 테스트 픽스처

    @Before
    public void setUp() {
        users = Arrays.asList(
                // new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                // new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                // new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                // new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                // new User("green", "오민규", "p5", Level.GOLD, 100, 100)
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGIN_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGIN_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    // userService 빈의 주입을 확인하는 테스트
    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }
    
    // 사용자 레벨 업그레이드 테스트
    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        userService.upgradeLevels();

        // 각 사용자별로 업그레이드 후의 예상 레벨을 검증한다.
        // checkLevel(users.get(0), Level.BASIC);
        // checkLevel(users.get(1), Level.SILVER);
        // checkLevel(users.get(2), Level.SILVER);
        // checkLevel(users.get(3), Level.GOLD);
        // checkLevel(users.get(4), Level.GOLD);
        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
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
    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }

    // 어떤 레벨로 바뀔 것인가가 아니라, 다음 레벨로 업그레이드할 것인가 아닌가를 지정하도록 개선한다.
    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            // 업그레이드가 일어났는지 확인한다.
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }
        else {
            // 업그레이드가 일어나지 않았는지 확인한다.
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }
}