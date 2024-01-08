package com.gaga.springtoby.factory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 팩토리 빈 테스트
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/FactoryBeanTest-context.xml")
public class FactoryBeanTest {

	@Autowired
	ApplicationContext context;

	// 팩토리 빈 테스트
	@Test
	public void getMessageFromFactoryBean() {
		// 팩토리 빈이 만들어주는 빈 오브젝트를 가져온다.
		Object message = context.getBean("message");
		// 타입 확인
		assertThat(message.getClass(), is(Message.class));
		// 설정과 기능 확인
		assertThat(((Message)message).getText(), is("Factory Bean"));
	}

	// 팩토리 빈을 가져오는 기능 테스트
	@Test
	public void getFactoryBean() throws Exception {
		// &가 붙고 안 붙고에 따라 getBean() 메소드가 돌려주는 오브젝트가 달라진다.
		// 팩토리 빈이 만들어주는 빈 오브젝트가 아니라 팩토리 빈 자체를 가져온다.
		Object factory = context.getBean("&message");
		assertThat(factory.getClass(), is(MessageFactoryBean.class));
	}
}
