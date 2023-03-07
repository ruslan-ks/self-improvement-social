package com.my.selfimprovement.controller;

import com.my.selfimprovement.dto.MinimalUserResponse;
import com.my.selfimprovement.dto.UserRegistrationRequest;
import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.UserService;
import com.my.selfimprovement.util.validation.UserRegistrationRequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private static final int PAGE_SIZE = 2;

    private final UserService userService;

    private final PasswordEncoder encoder;

    private final UserRegistrationRequestValidator userRegistrationRequestValidator;

    private final ModelMapper modelMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/new")
    public String signUpPage(@ModelAttribute("user") UserRegistrationRequest userRegistrationRequest) {
        return "users/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid UserRegistrationRequest userRegistrationRequest,
                         BindingResult bindingResult) {
        userRegistrationRequestValidator.validate(userRegistrationRequest, bindingResult);
        User user = toUser(userRegistrationRequest);
        if (bindingResult.hasErrors()) {
            for (var error : bindingResult.getFieldErrors()) {
                log.warn("Failed to register user! Invalid user property: {}", error.getDefaultMessage());
            }
            return "users/new";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

    private User toUser(UserRegistrationRequest userRegistrationRequest) {
        return modelMapper.map(userRegistrationRequest, User.class);
    }

    @GetMapping
    @ResponseBody
    public List<MinimalUserResponse> minimalUserResponsePage(@RequestParam("page") int pageIndex) {
        return userService.findActiveUsersPage(pageIndex, PAGE_SIZE)
                .map(u -> {
                    var dto = modelMapper.map(u, MinimalUserResponse.class);
                    dto.setActivityCount(u.getActivities().size());
                    return dto;
                })
                .toList();
    }

}
