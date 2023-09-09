package com.gaga.springtoby.user.dao;

/**
 * 직접 생성한 DaoFactory 오브젝트
 */
public class DaoFactoryTest {
    public static void main(String[] args) {

        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        // 매번 userDao를 호출하면 계속해서 새로운 오브젝트가 만들어진다.
        System.out.println(dao1); // 오브젝트 1
        System.out.println(dao2); // 오브젝트 2
    }
}
