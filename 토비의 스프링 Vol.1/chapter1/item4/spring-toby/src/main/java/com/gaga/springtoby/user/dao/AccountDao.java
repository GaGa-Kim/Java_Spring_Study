package com.gaga.springtoby.user.dao;

public class AccountDao {
    // 특정 클래스 대신 인터페이스를 사용해서 DB 커넥션을 가져와 사용한다.
    private ConnectionMaker connectionMaker;

    public AccountDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
