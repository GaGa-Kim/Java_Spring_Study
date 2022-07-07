package com.example.examplespring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * 다국어 프로퍼티를 사용하기 위한 메세지 소스를 빈으로 등록
 */
@Configuration
public class WebConfiguration {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:/messages/message");
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(60);
        source.setDefaultLocale(Locale.KOREA);
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}

