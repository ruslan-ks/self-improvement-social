package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

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
