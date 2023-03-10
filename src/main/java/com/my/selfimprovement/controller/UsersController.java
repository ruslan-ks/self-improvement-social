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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {

    private static final int DEFAULT_PAGE_SIZE = 2;

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

//    @PostMapping
//    public String create(@ModelAttribute("user") @Valid UserRegistrationRequest userRegistrationRequest,
//                         BindingResult bindingResult) {
//        userRegistrationRequestValidator.validate(userRegistrationRequest, bindingResult);
//        User user = toUser(userRegistrationRequest);
//        if (bindingResult.hasErrors()) {
//            logUserValidationErrors(bindingResult);
//            return "users/new";
//        }
//        user.setPassword(encoder.encode(user.getPassword()));
//        userService.save(user);
//        return "redirect:/login";
//    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        userRegistrationRequestValidator.validate(userRegistrationRequest);
        User user = toUser(userRegistrationRequest);
        user.setPassword(encoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    private User toUser(UserRegistrationRequest userRegistrationRequest) {
        return modelMapper.map(userRegistrationRequest, User.class);
    }

    @GetMapping
    @ResponseBody
    public List<MinimalUserResponse> minimalUserResponsePage(@RequestParam("page") int pageIndex,
            @RequestParam(value = "pageSize") Optional<Integer> pageSizeParam) {
        int pageSize = pageSizeParam.orElse(DEFAULT_PAGE_SIZE);
        return userService.findActiveUsersPage(pageIndex, pageSize)
                .map(u -> {
                    var dto = modelMapper.map(u, MinimalUserResponse.class);
                    dto.setActivityCount(u.getActivities().size());
                    return dto;
                })
                .toList();
    }

}
