package com.gaga.springtoby.proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * 리플렉션 학습 테스트
 */
public class ReflectionTest {
	@Test
	public void invokeMethod() throws Exception {
		String name = "Spring";

		// String 클래스의 length() 메소드를 코드에서 직접 호출
		assertThat(name.length(), is(6));

		// String 클래스의 length() 메소드를 Method를 이용해 리플렉션 호출
		Method lengthMethod = String.class.getMethod("length");
		assertThat((Integer)lengthMethod.invoke(name), is(6));

		// String 클래스의 charAt() 메소드를 코드에서 직접 호출
		assertThat(name.charAt(0), is('S'));

		// String 클래스의 charAt() 메소드를 Method를 이용해 리플렉션 호출
		Method charAtMethod = String.class.getMethod("charAt", int.class);
		assertThat((Character)charAtMethod.invoke(name, 0), is('S'));
	}
}
