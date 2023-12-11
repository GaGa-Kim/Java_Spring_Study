package com.gaga.springtoby.user.domain;

/**
 * 사용자 정보 저장용 자바빈 User 클래스
 */
public class User {

    /* User 오브젝트에 담긴 정보가 실제 보관된 DB 테이블
    create table users (
        id varchar(10) primary key,
        name varchar(20) not null,
        password varchar(10) not null,
        level tinyint not null,
        login int not null,
        recommend int not null
    )
     */

    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;

    // 파라미터가 있는 User 클래스 생성자
    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;

    }

    // 자바빈의 규약을 따르는 클래스에 생성자를 명시적으로 추가할 때의 파라미터가 없는 디폴트 생성자
    public User() {
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    // User의 레벨 업그레이드 작업용 메소드
    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        }
        else {
            this.level = nextLevel;
        }
    }
}
