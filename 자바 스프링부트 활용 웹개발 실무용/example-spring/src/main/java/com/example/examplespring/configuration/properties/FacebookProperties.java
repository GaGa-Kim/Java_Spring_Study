package com.example.examplespring.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties
public class FacebookProperties {

    private String restapi;
    private String javascript;
    private String clientSecret;

}
