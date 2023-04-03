package com.my.selfimprovement;

import com.my.selfimprovement.security.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class SelfImprovementSocialApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelfImprovementSocialApplication.class, args);
    }

}
