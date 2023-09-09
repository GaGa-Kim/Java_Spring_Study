package com.gaga.springtoby.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * 스프링 빈 팩토리가 사용할 설정정보를 담은 DaoFactory 클래스
 */
@Configuration // 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {
    
    @Bean // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
    public UserDao userDao() throws ClassNotFoundException {
        // UserDao 오브젝트를 생성한다. 그리고 사용할 ConnectionMaker 타입의 오브젝트를 제공한다.
        // 결국 두 오브젝트 사이의 의존관계 설정 효과
        UserDao useDao = new UserDao();
        // useDao.setConnectionMaker(connectionMaker());
        useDao.setDataSource(dataSource());
        return useDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        // UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
        // 분리해서 중복을 제거
        return new DConnectionMaker(); // D사
        // return new LocalDBConnectionMaker(); // 개발용
        // return new ProductionDBConnectionMaker(); // 운영용
    }

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        // DB 연결정보
        dataSource.setDriverClass((Class<Driver>) Class.forName("com.mysql.jdbc.Driver"));
        dataSource.setUrl("jdbc:mysql://localhost/toby");
        dataSource.setUsername("toby");
        dataSource.setPassword("gaga");

        return dataSource;
    }
}
