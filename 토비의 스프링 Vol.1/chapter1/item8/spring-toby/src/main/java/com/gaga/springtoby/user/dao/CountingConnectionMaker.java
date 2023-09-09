package com.gaga.springtoby.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 연결횟수 카운팅 기능이 있는 ConnectionMaker 구현 클래스
 */
public class CountingConnectionMaker implements ConnectionMaker {
    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    // 모든 DAO의 makeConnection() 메소드를 호출하는 부분에 카운터를 증가시키는 코드를 추가한다.
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        // DB 연결횟수 카운터를 증가
        this.counter++;
        return realConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return this.counter;
    }
}
