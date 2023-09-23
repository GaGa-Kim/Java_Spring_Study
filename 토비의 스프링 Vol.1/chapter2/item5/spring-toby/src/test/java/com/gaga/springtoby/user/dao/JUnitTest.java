package com.gaga.springtoby.user.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * 스프링 테스트 컨텍스트에 대한 학습 테스트
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JUnitTest {

    // 테스트 컨텍스트가 매번 주입해주는 애플리케이션 컨텍스트는 항상 같은 오브젝트인지 테스트로 확인해본다.
    @Autowired
    private ApplicationContext context;

    // 매 테스트 메소드에서 현재 스태틱 변수에 담긴 오브젝트와 자신을 비교해서 같지 않다는 사실을 확인한다.
    static Set<JUnitTest> testObjects = new HashSet<JUnitTest>(); // 테스트 오브젝트를 저장할 수 있는 컬렉션
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        // 테스트마다 현재 테스트 오브젝트가 컬렉션에 이미 등록되어 있는지 확인하고,
        assertThat(testObjects, not(hasItem(this)));
        // 없으면 자기 자신을 추가한다.
        testObjects.add(this);

        // null이라면 첫 번째 테스트이므로 통과시키고
        assertThat(contextObject == null || contextObject == this.context, is(true));
        // contextObject에 현재 context를 저장해둔다.
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        // 다음부터는 저장된 contextObject가 null이 아닐 테니 현재의 context가 같은지 비교하며
        assertTrue(contextObject == null || contextObject == this.context);
        // 한 번이라도 다른 오브젝트가 나오면 테스트는 실패한다.
        contextObject = this.context;
    }

    @Test
    public void test3() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        // 위 셋 모두 동일한 검증이지만 방법이 다르다.
        // 이후 세 가지 방법 중 사용하기 편리하다고 생각되는 것을 선택해서 사용한다.
        assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
        contextObject = this.context;
    }
}
