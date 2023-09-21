package com.gaga.springtoby.user.dao;


import com.gaga.springtoby.user.domain.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

/**
 * 테스트용 main() 메소드
 */
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // XML 애플리케이션 컨텍스트로 UserDao 오브젝트를 받아온다.
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
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

        // add()에 전달한 User 오브젝트에 담긴 사용자 정보와
        // get()을 통해 다시 DB에 가져온 User 오브젝트의 정보가 서로 정확히 일치하는가 확인한다.
        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        }
        else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        }
        else {
            System.out.println("조회 테스트 성공");
        }
    }
}
