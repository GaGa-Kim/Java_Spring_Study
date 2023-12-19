package com.gaga.springtoby.user.service;

import com.gaga.springtoby.user.domain.User;

/**
 * UserService 인터페이스
 */
public interface UserService {
	void add(User user);

	void upgradeLevels();
}
