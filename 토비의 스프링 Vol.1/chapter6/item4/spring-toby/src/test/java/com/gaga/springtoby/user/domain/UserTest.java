package com.gaga.springtoby.user.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * User 테스트
 */
public class UserTest {
    User user;

    // 컨테이너가 생성한 오브젝트를 @Autowired로 가져오는 대신
    // 생성자를 호출해서 테스트할 User 오브젝트를 생성한다.
    @Before
    public void setUp() {
        user = new User();
    }

    // 업그레이드 가능 작업 테스트
    @Test
    public void upgradeLevel() {
        // Level 이늄에 정의된 모든 레벨을 가져와서 User에 설정해둔다.
        Level[] levels = Level.values();
        // 다음 레벨로 바뀌는지 확인한다.
        for (Level level : levels) {
            // 다음 단계가 null인 경우는 제외한다.
            if (level.nextLevel() == null)
                continue;
            user.setLevel(level);
            user.upgradeLevel();
            assertThat(user.getLevel(), is(level.nextLevel()));
        }
    }

    // 업그레이드 작업 실패 테스트
    @Test(expected = IllegalStateException.class)
    public void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        // 더 이상 업그레이드할 레벨이 없는 경우에 예외가 발생하는지 확인한다.
        for (Level level : levels) {
            if (level.nextLevel() != null)
                continue;
            user.setLevel(level);
            user.upgradeLevel();
        }
    }
}