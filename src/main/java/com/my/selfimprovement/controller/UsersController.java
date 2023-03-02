package com.my.selfimprovement.controller;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private final UserService userService;

    private final PasswordEncoder encoder;

    @GetMapping("/new")
    public String signUpPage(@ModelAttribute("user") User user) {
        return "users/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (var error : bindingResult.getFieldErrors()) {
                log.warn("Invalid user property: {}", error.getDefaultMessage());
            }
            return "users/new";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

}
