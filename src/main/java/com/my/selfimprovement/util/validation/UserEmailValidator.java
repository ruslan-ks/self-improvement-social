package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Contains user email validation logic
 */
@Component
@RequiredArgsConstructor
public class UserEmailValidator {

    private final UserService userService;

    private final MessageSource messageSource;

    public void validate(String email, Errors errors) {
        if (email != null && userService.findByEmail(email).isPresent()) {
            String message = messageSource.getMessage("valid.user.email.inUse", null,
                    LocaleContextHolder.getLocale());
            errors.rejectValue("email", "valid.user.email.inUse", message);
        }
    }

}
