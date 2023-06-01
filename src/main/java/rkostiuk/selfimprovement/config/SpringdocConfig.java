package rkostiuk.selfimprovement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        var info = new Info()
                .title("Self-improvement social network REST API")
                .version("1.0.0")
                .description("REST API built with Spring");
        return new OpenAPI().info(info);
    }

}
