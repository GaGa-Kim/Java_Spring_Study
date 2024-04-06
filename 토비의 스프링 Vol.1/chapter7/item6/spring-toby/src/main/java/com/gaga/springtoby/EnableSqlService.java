package com.gaga.springtoby;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * @Import를 메타 애노테이션으로 넣은 애노테이션 정의
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {
}