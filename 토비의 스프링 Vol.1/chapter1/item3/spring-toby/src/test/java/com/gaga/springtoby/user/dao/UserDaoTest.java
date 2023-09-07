package com.gaga.springtoby.user.dao;


import com.gaga.springtoby.user.domain.User;

import java.sql.SQLException;

/**
 * 테스트용 main() 메소드
 */
public class UserDaoTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
        ConnectionMaker connectionMaker = new DConnectionMaker();

        // UserDao 오브젝트를 생성한다. 그리고 사용할 ConnectionMaker 타입의 오브젝트를 제공한다.
        // 결국 두 오브젝트 사이의 의존관계 설정 효과
        UserDao userDao = new UserDao(connectionMaker);

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
    }
}
