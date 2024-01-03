package com.gaga.springtoby.user.service;

import java.util.List;

import com.gaga.springtoby.user.domain.User;

/**
 * UserService 인터페이스
 */
public interface UserService {
	void add(User user);

	User get(String id);

	List<User> getAll();

	void deleteAll();

	void update(User user);

	void upgradeLevels();
}
