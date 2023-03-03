package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;
import com.my.selfimprovement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var user = (User) target;
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            String message = messageSource.getMessage("valid.user.email.inUse", null,
                    LocaleContextHolder.getLocale());
            errors.rejectValue("email", "valid.user.email.inUse", message);
        }
    }

}
