package com.gaga.springtoby.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CountingConnectionMaker 의존관계가 추가된 DI 설정용 클래스
 */
@Configuration // 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class CountingDaoFactory {

    @Bean // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
    public UserDao userDao() {
        // UserDao 오브젝트를 생성한다. 그리고 사용할 ConnectionMaker 타입의 오브젝트를 제공한다.
        // 결국 두 오브젝트 사이의 의존관계 설정 효과
        UserDao useDao = new UserDao();
        useDao.setConnectionMaker(connectionMaker());
        return useDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        // CountingConnectionMaker 타입의 오브젝트를 생성하게 만든다.
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        // CountingConnectionMaker 타입의 오브젝트를 통해 실제 DB 커넥션을 만들어주는 메소드를 생성한다.
        // UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
        return new DConnectionMaker(); // D사
    }
}

