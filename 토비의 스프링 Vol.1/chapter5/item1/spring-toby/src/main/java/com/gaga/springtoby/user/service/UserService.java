package com.gaga.springtoby.user.service;

import com.gaga.springtoby.user.dao.UserDao;
import com.gaga.springtoby.user.domain.Level;
import com.gaga.springtoby.user.domain.User;
import java.util.List;

/**
 * UserService 클래스
 */
public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    // UserDao 인터페이스 타입으로 userDao 빈을 DI 받아 사용한다.
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 사용자 레벨 업그레이드 메소드
    /* 여러 문제점이 존재하므로 코드 개선이 필요
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            // 레벨의 변화가 있는지 확인하는 플러그
            Boolean changed = null;
            // BASIC 레벨 업그레이드 작업
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            }
            // SILVER 레벨 업그레이드 작업
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }
            // GOLD 레벨은 변경이 일어나지 않는다.
            else if (user.getLevel() == Level.GOLD) {
                changed = false;
            }
            // 일치하는 조건이 없으면 변경 없음
            else {
                changed = false;
            }
            // 레벨의 변경이 있은 경우에만 update() 호출
            if (changed) {
                userDao.update(user);
            }
        }
    } */

    // 사용자 레벨 업그레이드 메소드
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    // 업그레이드 가능 확인 메소드
    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        // 레벨별로 구분해서 조건을 판단한다.
        switch (currentLevel) {
            // case BASIC: return (user.getLogin() >= 50);
            case BASIC: return (user.getLogin() >= MIN_LOGIN_FOR_SILVER);
            // case SILVER: return (user.getRecommend() >= 30);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            // 현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다.
            // 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 나서 확인할 수 있다.
            default: throw new IllegalArgumentException("Unknown level: " + currentLevel);
        }
    }

    // 레벨 업그레이드
    private void upgradeLevel(User user) {
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
