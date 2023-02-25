package com.my.selfimprovement.controller;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign-up")
@RequiredArgsConstructor
public class SignUpController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    // TODO: delete this method
    @PostConstruct
    public void init() {
        if (userService.findAll().stream().noneMatch(u -> u.getRole() == User.Role.ROOT)) {
            var user = new User();
            user.setEmail("root@root.com");

            user.setPassword(passwordEncoder.encode("pass"));

            user.setName("Root");
            user.setRole(User.Role.ROOT);
            userService.save(user);
        }
    }

}
