package com.gaga.springtoby.proxy;

/**
 * 프록시 클래스
 */
public class HelloUppercase implements Hello {
	// 위임할 타깃 오브젝트
	// 다른 프록시를 추가할 수도 있으므로 인터페이스로 접근한다.
	Hello hello;

	public HelloUppercase(Hello hello) {
		this.hello = hello;
	}

	public String sayHello(String name) {
		// 위임과 부가기능 적용
		return hello.sayHello(name).toUpperCase();
	}

	public String sayHi(String name) {
		return hello.sayHi(name).toUpperCase();
	}

	public String sayThankYou(String name) {
		return hello.sayThankYou(name).toUpperCase();
	}
}
