package com.gaga.springtoby.proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

/**
 * 스프링 ProxyFactoryBean을 이용한 다이내믹 프록시 테스트
 */
public class DynamicProxyTest {

	// 자바 JDK의 다이내믹 프록시 생성
	@Test
	public void simpleProxy() {
		Hello proxiedHello = (Hello)Proxy.newProxyInstance(
			getClass().getClassLoader(), // 동적으로 생성되는 다이내믹 프록시 클래스의 로딩에 사용할 클래스 로드
			new Class[] {Hello.class}, // 구현할 인터페이스
			new UppercaseHandler(new HelloTarget())); // 부가기능과 위임 코드를 담은 InvocationHandler 구현 오브젝트
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}

	// 스프링의 다이내믹 프록시 생성
	@Test
	public void proxyFactoryBean() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget()); // 타깃 설정
		pfBean.addAdvice(new UppercaseAdvice()); // 부가기능을 담은 어드바이스를 추가, 여러 개 추가 가능

		Hello proxiedHello = (Hello)pfBean.getObject(); // FactoryBean이므로 getObject()로 생성된 팩토리를 가져옴
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		assertThat(proxiedHello.sayThankYou("Toby"), is("THANK YOU TOBY"));
	}

	// 포인트컷까지 적용한 ProxyFactoryBean
	@Test
	public void pointcutAdvisor() {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(new HelloTarget()); // 타깃 설정

		// 메소드 이름을 비교해서 대상을 선정하는 알고리즘을 제공하는 포인트컷 생성
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		// 이름 비교조건 설정
		pointcut.setMappedName("sayH*"); // sayH로 시작하는 모든 메소드를 선택

		// 포인트컷과 어드바이스를 Advisor로 묶어서 한 번에 추가
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

		Hello proxiedHello = (Hello)pfBean.getObject();
		assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
		assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
		// 메소드 이름이 포인트컷의 선정조건에 맞지 않으므로, 부가기능이 적용되지 않는다.
		assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby"));
	}

	// 확장 포인트컷까지 적용한 ProxyFactoryBean
	@Test
	public void claasNamePointcutAdvisor() {
		// 포인트컷 준비
		NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
			// 익명 내부 클래스 방식으로 클래스를 정의한다.
			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					@Override
					public boolean matches(Class<?> clazz) {
						// 클래스 이름이 HelloT로 시작하는 것만 선정한다. (클래스 필터)
						return clazz.getSimpleName().startsWith("HelloT");
					}
				};
			}
		};
		// sayH로 시작하는 메소드 이름을 가진 메소드만 선정한다. (메소드 매처)
		classMethodPointcut.setMappedName("sayH*");

		// 적용 클래스다. (HelloT로 시작)
		checkAdviced(new HelloTarget(), classMethodPointcut, true);

		// 적용 클래스가 아니다.
		class HelloWorld extends HelloTarget {
		}
		checkAdviced(new HelloWorld(), classMethodPointcut, false);

		// 적용 클래스다. (HelloT로 시작)
		class HelloToby extends HelloTarget {
		}
		checkAdviced(new HelloToby(), classMethodPointcut, true);
	}

	// 적용 대상인지 확인
	private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
		ProxyFactoryBean pfBean = new ProxyFactoryBean();
		pfBean.setTarget(target); // 타깃 설정
		pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice())); // 포인트컷과 어드바이스를 묶어서 한 번에 추가
		Hello proxiedHello = (Hello)pfBean.getObject(); // FactoryBean이므로 getObject()로 생성된 팩토리를 가져옴

		if (adviced) {
			// 메소드 선정 방식을 통해 어드바이스 적용
			assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY")); // 클래스 필터 O + 메소드 매처 O
			assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY")); // 클래스 필터 O + 메소드 매처 O
			assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby")); // 클래스 필터 O + 메소드 매처 X
		} else {
			// 어드바이스 적용 대상 후보에서 아예 탈락
			assertThat(proxiedHello.sayHello("Toby"), is("Hello Toby")); // 클래스 필터 X
			assertThat(proxiedHello.sayHi("Toby"), is("Hi Toby")); // 클래스 필터 X
			assertThat(proxiedHello.sayThankYou("Toby"), is("Thank You Toby")); // 클래스 필터 X
		}
	}
}