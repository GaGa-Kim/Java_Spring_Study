package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

/**
 * CountingConnectionMaker에 대한 테스트 클래스
 */
public class UserDaoConnectionCountingTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // 애플리케이션 컨텍스트로 UserDao 오브젝트를 받아온다.
        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao userDao = context.getBean("userDao", UserDao.class);

        // 프로퍼티에 값 넣는다.
        User user = new User();
        user.setId("gaga");
        user.setName("김가경");
        user.setPassword("gagakim");

        // add() 메소드를 이용해 DB에 등록해본다.
        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        // get() 메소드를 이용해 DB에 저장된 결과를 가져와 본다.
        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

        // 의존관계 검색으로 CountingConnectionMaker 오브젝트를 가져온다.
        CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
        // DAO를 통해 DB 커넥션을 요청한 횟수(2)만큼 카운터가 증가된다.
        System.out.println("Connection counter : " + ccm.getCounter());
    }
}