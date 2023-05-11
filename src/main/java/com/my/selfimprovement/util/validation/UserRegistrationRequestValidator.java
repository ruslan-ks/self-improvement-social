package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.util.validation.abstracts.ControllerLayerValidator;

public abstract class UserRegistrationRequestValidator extends ControllerLayerValidator<UserRegistrationRequest> {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationRequest.class.equals(aClass);
    }

}
