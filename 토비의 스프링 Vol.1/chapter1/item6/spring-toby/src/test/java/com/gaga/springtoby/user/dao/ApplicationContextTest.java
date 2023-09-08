package com.gaga.springtoby.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 스프링 컨텍스트로부터 가져온 오브젝트
 */
public class ApplicationContextTest {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao1 = context.getBean("userDao", UserDao.class);
        UserDao dao2 = context.getBean("userDao", UserDao.class);

        // 매번 userDao를 호출하면 동일한 오브젝트를 돌려받게 된다.
        System.out.println(dao1); // 오브젝트 1
        System.out.println(dao2); // 오브젝트 1
    }
}
