package com.example.examplespring.configuration;

import com.example.examplespring.configuration.servlet.handler.BaseHandlerInterceptor;
import com.example.examplespring.framework.data.web.MySQLRequestHandleMethodArgumentResolver;
import com.example.examplespring.mvc.domain.BaseCodeLabelEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;
import java.util.Locale;

/**
 * 다국어 프로퍼티를 사용하기 위한 메세지 소스를 빈으로 등록
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

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

    // WebConfiguration 클래스에 WebMvcConfigurer 인터페이스 구현
    @Bean
    public BaseHandlerInterceptor baseHandlerInterceptor() {
        return new BaseHandlerInterceptor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BaseCodeLabelEnum.class, new BaseCodeLabelEnumJsonSerializer());
        return objectMapper;
    }

    @Bean
    public MappingJackson2JsonView mappingJackson2JsonView() {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setContentType(MediaType.APPLICATION_JSON_VALUE);
        jsonView.setObjectMapper(objectMapper());
        return jsonView;
    }

    @Bean
    public GlobalConfig config() {
        return new GlobalConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(baseHandlerInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 페이지 리졸버 등록
        resolvers.add(new MySQLRequestHandleMethodArgumentResolver());
    }

    // 브라우저에서 접근 가능 (URL로 업로드 파일 보기 가능)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드 파일 static resource 접근 경로
        String resourcePattern = config().getUploadResourcePath() + "**";
        // 로컬 (윈도우 환경)
        if (config().isLocal()) {
            registry.addResourceHandler(resourcePattern).addResourceLocations("file:///" + config().getUploadFilePath());
        } else {
            // 리눅스 도는 유닉스 환경
            registry.addResourceHandler(resourcePattern).addResourceLocations("file:" + config().getUploadFilePath());
        }
    }
}

