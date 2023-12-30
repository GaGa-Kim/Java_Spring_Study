package com.gaga.springtoby.proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.junit.Test;

/**
 * 클라이언트 역할의 테스트
 */
public class HelloTest {

	@Test
	public void simpleProxy() {
		// 타깃은 인터페이스를 통해 접근하는 습관을 들이자.
		Hello hello = new HelloTarget();
		assertThat(hello.sayHello("Toby"), is("Hello Toby"));
		assertThat(hello.sayHi("Toby"), is("Hi Toby"));
		assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
	}

	@Test
	public void helloUppercaseProxy() {
		// 프록시를 통해 타깃 오브젝트에 접근하도록 구성한다.
		Hello proxiedHello = new HelloUppercase(new HelloTarget());
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}

	@Test
	public void upperCaseHandlerProxy() {
		// 생성된 다이내믹 프록시 오브젝트는 Hello 인터페이스를 구현하고 있으므로 Hello 타입으로 캐스팅해도 안전하다.
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
			getClass().getClassLoader(), // 동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로드
			new Class[] {Hello.class}, // 구현할 인터페이스
			new UppercaseHandler(new HelloTarget())); // 부가기능과 위임 코드를 담은 InvocationHandler 구현 오브젝트
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}
}
