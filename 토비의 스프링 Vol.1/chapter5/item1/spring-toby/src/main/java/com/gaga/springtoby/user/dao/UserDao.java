package com.gaga.springtoby.user.dao;

import com.gaga.springtoby.user.domain.User;

import java.util.List;

/**
 * UserDao 인터페이스
 */
public interface UserDao {
    void add(User user);
    void update(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
}
