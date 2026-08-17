package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "recaptcha")
public class RecaptchaProperties {

    private String siteKey;

    private String secretKey;

    private Double scoreThreshold = 0.5;

    private String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

    private boolean enabled = true;
}