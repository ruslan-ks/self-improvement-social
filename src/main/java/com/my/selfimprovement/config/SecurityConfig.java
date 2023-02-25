package com.my.selfimprovement.config;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.security.UserDetailsServiceImpl;
import com.my.selfimprovement.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String LOGIN_URL = "/login";

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/root/**").hasRole(User.Role.ROOT.name())
                    .requestMatchers("/admin/**").hasAnyRole(User.Role.ADMIN.name(), User.Role.ROOT.name())
                    .requestMatchers("/user/**").authenticated()
                    .requestMatchers(LOGIN_URL).anonymous()
                    .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage(LOGIN_URL)
                    .usernameParameter("email")
                    .defaultSuccessUrl("/user")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl(LOGIN_URL)
                .and()
                .build();
    }

}
