package com.gaga.springtoby;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.gaga.springtoby.user.dao.UserDao;

/**
 * SqlMapConfig 인터페이스를 구현한 클래스
 */
public class UserSqlMapConfig implements SqlMapConfig {
	@Override
	public Resource getSqlMapResource() {
		return new ClassPathResource("/usersqlmap.xml", UserDao.class);
	}
}
