package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class UserValidatorImpl extends UserValidator {

    private final UserEmailValidator userEmailValidator;

    @Override
    public void validate(Object target, Errors errors) {
        var user = (User) target;
        userEmailValidator.validate(user.getEmail(), errors);
    }

}
