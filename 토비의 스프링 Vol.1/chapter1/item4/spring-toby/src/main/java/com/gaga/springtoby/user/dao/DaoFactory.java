package com.gaga.springtoby.user.dao;

/**
 * UserDao의 생성 책임을 맡은 팩토리 클래스
 */
public class DaoFactory {
    public UserDao userDao() {
        // UserDao 오브젝트를 생성한다. 그리고 사용할 ConnectionMaker 타입의 오브젝트를 제공한다.
        // 결국 두 오브젝트 사이의 의존관계 설정 효과
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    // UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
    public ConnectionMaker connectionMaker() {
        // 분리해서 중복을 제거
        return new DConnectionMaker(); // D사
    }
}
