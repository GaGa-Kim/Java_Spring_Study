package com.example.examplespring.configuration;

import com.example.examplespring.configuration.properties.FacebookProperties;
import com.example.examplespring.configuration.properties.KakaoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@EnableConfigurationProperties(
        {KakaoProperties.class, FacebookProperties.class}
)
@PropertySources({
        @PropertySource("classpath:properties/kakao.properties"),
        @PropertySource("classpath:properties/facebook.properties")
})
public class PropertiesConfiguration {
}