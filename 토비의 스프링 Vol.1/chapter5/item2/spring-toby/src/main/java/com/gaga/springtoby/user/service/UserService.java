package com.gaga.springtoby.user.service;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * UserService 클래스
 */
public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    // private DataSource dataSource;
    private PlatformTransactionManager transactionManager;
    UserDao userDao;

    /* Connection을 생성할 때 사용할 DataSource를 DI 받도록 한다.
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    } */

    // UserDao 인터페이스 타입으로 userDao 빈을 DI 받아 사용한다.
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /* 사용자 레벨 업그레이드 메소드
    // 트랜잭션 동기화 방식을 적용
    public void upgradeLevels() throws Exception {
        // 트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화한다.
        TransactionSynchronizationManager.initSynchronization();
        // DB 커넥션을 생성하고 트랜잭션을 시작한다.
        // 이후의 DAO 작업은 모두 여기서 시작한 트랜잭션 안에서 진행된다.
        Connection c = DataSourceUtils.getConnection(dataSource); // DB 커넥션 생성과 동기화를 함께 해주는 유틸리티 메소드
        c.setAutoCommit(false);

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            // 정상적으로 작업을 마치면 트랜잭션 커밋
            c.commit();
        } catch (Exception e) {
            // 예외가 발생하면 롤백
            c.rollback();
            throw e;
        } finally {
            // 유틸리티 메소드를 이용해 DB 커넥션을 안전하게 닫는다.
            DataSourceUtils.releaseConnection(c, dataSource);
            // 동기화 작업 종료 및 정리
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    } */

    // 사용자 레벨 업그레이드 메소드
    // 스프링의 트랜잭션 추상화 API를 적용
    public void upgradeLevels() throws Exception {
        // 트랜잭션 매니저를 빈으로 분리시킨 후 DI 받아 트랜잭션 시작
        // DI 받은 트랜잭션 매니저를 공유해서 사용하므로 멀티스레드 환경에서도 안전한다.
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        // 트랜잭션 안에서 진행되는 작업
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            // 정상적으로 작업을 마치면 트랜잭션 커밋
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            // 예외가 발생하면 롤백
            transactionManager.rollback(status);
            throw e;
        }
    }

    // 업그레이드 가능 확인 메소드
    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        // 레벨별로 구분해서 조건을 판단한다.
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGIN_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            // 현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다.
            // 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 나서 확인할 수 있다.
            default: throw new IllegalArgumentException("Unknown level: " + currentLevel);
        }
    }

    // 레벨 업그레이드
    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    // 사용자 신규 등록 로직
    public void add(User user) {
        if (user.getLevel() == null)
            user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
