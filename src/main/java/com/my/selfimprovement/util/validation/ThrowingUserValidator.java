package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;

public interface ThrowingUserValidator {

    /**
     * Calls custom validators and throws {@link jakarta.validation.ConstraintViolationException} if validation fails
     * @param user object to be validate
     */
    void validate(User user);

}
