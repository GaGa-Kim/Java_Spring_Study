package com.gaga.springtoby;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

import com.gaga.springtoby.user.service.DummyMailSender;
import com.gaga.springtoby.user.service.TestUserService;
import com.gaga.springtoby.user.service.UserService;

/**
 * 분리한 테스트 DI 정보
 */
@Configuration
@Profile("test")
public class TestAppContext {
	// @Autowired
	// UserDao userDao;

	/* testUserService 빈 정의
	<bean id="testUserService"
          class="com.gaga.springtoby.user.service.TestUserService"
          parent="userService"/>
	 */
	@Bean
	public UserService testUserService() {
		/*
		TestUserService testUserService = new TestUserService();
		testUserService.setUserDao(this.userDao);
		testUserService.setMailSender(mailSender());
		return testUserService;
		 */
		return new TestUserService();
	}

	/* mailSender 빈 정의
	<bean id="mailSender" class="com.gaga.springtoby.user.service.DummyMailSender"/>
	 */
	@Bean
	public MailSender mailSender() {
		return new DummyMailSender();
	}
}
