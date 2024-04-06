package com.gaga.springtoby.user.service;

import java.util.List;

import com.gaga.springtoby.user.domain.User;

/**
 * TestApplicationContext에 사용하기 위해 test 패키지의 UserServiceTest에서 가져옴
 */
public class TestUserService extends UserServiceImpl {
	private String id = "madnite1";

	protected void upgradeLevel(User user) {
		if (user.getId().equals(this.id))
			throw new TestUserServiceException();
		super.upgradeLevel(user);
	}

	public List<User> getAll() {
		for (User user : super.getAll()) {
			super.update(user);
		}
		return null;
	}

	static class TestUserServiceException extends RuntimeException {
	}
}