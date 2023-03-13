package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class UserEmailValidatorImpl implements UserEmailValidator {

    private final MessageSource messageSource;

    private UserService userService;

    @Autowired
    @Lazy
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(String email, Errors errors) {
        if (email != null && userService.findByEmail(email).isPresent()) {
            String message = messageSource.getMessage("valid.user.email.inUse", null,
                    LocaleContextHolder.getLocale());
            errors.rejectValue("email", "valid.user.email.inUse", message);
        }
    }

}
