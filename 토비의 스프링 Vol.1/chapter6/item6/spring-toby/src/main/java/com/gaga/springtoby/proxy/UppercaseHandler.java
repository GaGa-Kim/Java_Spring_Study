package com.gaga.springtoby.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationHandler 구현 클래스
 */
public class UppercaseHandler implements InvocationHandler {
	// 다이내믹 프록시로부터 전달받은 요청을 다시 오브젝트에 위임해야 하기 때문에 타깃 오브젝트를 주입받아 둔다.
	// 어떤 종류의 인터페이스를 구현한 타깃에도 적용 가능하도록 Object 타입으로 수정하여 확장
	Object target; // Hello target 에서 확장

	public UppercaseHandler(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 타깃으로 위임, 인터페이스의 메소드 호출에 모두 적용된다.
		Object ret = (String)method.invoke(target, args);
		// 호출한 메소드의 리턴 타입이 String이며 리턴 타입과 메소드 이름이 일치하는 경우에만 대문자 변경 기능을 적용하도록 수정
		if (ret instanceof String && method.getName().startsWith("say")) {
			// 부가기능 제공 및 리턴된 값은 다이내믹 프록시가 받아서 최종적으로 클라이언트에게 전달된다.
			return ((String)ret).toUpperCase();
		} else {
			// 조건이 일치하지 않으면 타깃 오브젝트의 호출 결과를 그대로 리턴한다.
			return ret;
		}
	}
}
