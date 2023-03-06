package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

@Component
@RequiredArgsConstructor
public class ThrowingUserValidatorImpl implements ThrowingUserValidator {

    private final UserValidator userValidator;

    @Override
    public void validate(User user) {
        var builder = new StringBuilder();
        var bindingResult = new BeanPropertyBindingResult(user, "user");
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(err -> builder.append(err.getField())
                    .append(": ")
                    .append(err.getRejectedValue())
                    .append(" - ")
                    .append(err.getDefaultMessage()));
            throw new ConstraintViolationException(builder.toString(), null);
        }
    }

}
