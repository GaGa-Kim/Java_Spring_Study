package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * JUnit을 적용한 UserDaoTest
 */
public class UserDaoJUnitTest {

    // JUnit에게 테스트용 메소드임을 알려준다.
    @Test
    // JUnit 테스트 메소드는 반드시 public으로 선언돼야 한다.
    public void addAndGet() throws SQLException, ClassNotFoundException {

        // XML 애플리케이션 컨텍스트로 UserDao 오브젝트를 받아온다.
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao userDao = context.getBean("userDao", UserDao.class);

        // 프로퍼티에 값 넣는다.
        User user = new User();
        user.setId("gaging");
        user.setName("김가깅");
        user.setPassword("gagingkim");

        // add() 메소드를 이용해 DB에 등록해본다.
        userDao.add(user);

        // get() 메소드를 이용해 DB에 저장된 결과를 가져와 본다.
        User user2 = userDao.get(user.getId());

        // 첫 번째 파라미터의 값을 뒤에 나오는 매처 조건으로 비교해서 일치하면 다음으로 넘어간다.
        // if (!user.getName().equals(user2.getName())) { ... }
        assertThat(user2.getName(), is(user.getName()));
        // else if (!user.getPassword().equals(user2.getPassword())) { ... }
        assertThat(user2.getPassword(), is(user.getPassword()));
    }

    public static void main(String[] args) {
        JUnitCore.main("com.gaga.springtoby.user.dao.UserDaoJUnitTest");
    }
}