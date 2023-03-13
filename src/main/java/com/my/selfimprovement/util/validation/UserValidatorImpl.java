package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl implements UserValidator {

    private final UserEmailValidator userEmailValidator;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var user = (User) target;
        userEmailValidator.validate(user.getEmail(), errors);
    }

}
