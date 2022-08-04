package com.example.examplespring.configuration;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * 개발, 로컬, 운영에 따른 프로퍼티 파일의 내용을 변수에 매핑하는 클래스
 * 백엔드 서버 개발을 할 때 파일 업로드, restapi 인증정보, 각종 설정값을 관리
 * 이후 Controller, Service, Interceptor, Scheduler 등에서 @Autowired를 사용해 간단하게 사용
 * 수정 사항이 발생할 시 프로퍼티 파일의 값 또는 변수만 변경하면 됨
 * @author gagyeong
 */
public class GlobalConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ResourceLoader resourceLoader;

    private String uploadFilePath;

    private boolean local;
    private boolean dev;
    private boolean prod;

    @PostConstruct
    public void init() {
        logger.info("init");
        String[] activeProfiles = context.getEnvironment().getActiveProfiles(); // 프로필(local, prod, dev)에 따라 프로퍼티 파일을 가져옴
        String activeProfile = "local"; // 기본값
        if (ObjectUtils.isNotEmpty(activeProfiles)) {
            activeProfile = activeProfiles[0];
        }
        String resourcePath = String.format("classpath:globals/global-%s.properties", activeProfile);
        try {
            Resource resource = resourceLoader.getResource(resourcePath);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            // 프로퍼티 파일에 있는 uploadFile.path가 변수에 저장됨
            uploadFilePath = properties.getProperty("uploadFile.path");
            // local로 서버가 올라올 경우 /home/upload
            this.local = activeProfile.equals("local");
            // dev로 서버가 올라올 경우 /app/upload
            this.dev = activeProfile.equals("dev");
            // prod로 서버가 올라올 경우 /root/upload
            this.prod = activeProfile.equals("prod");

        } catch (Exception e) {
            logger.error("e", e);
        }
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public boolean isLocal() {
        return local;
    }

    public boolean isDev() {
        return dev;
    }

    public boolean isProd() {
        return prod;
    }
}
