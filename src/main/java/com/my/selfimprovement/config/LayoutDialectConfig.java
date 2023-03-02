package com.my.selfimprovement.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Layout config class<br>
 * This app uses Thymeleaf Page Layouts for pages generating.
 * Layouts reside in src/main/resources/templates/layouts.
 */
@Configuration
public class LayoutDialectConfig {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
