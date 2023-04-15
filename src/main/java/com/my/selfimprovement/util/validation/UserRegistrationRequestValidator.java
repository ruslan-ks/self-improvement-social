package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.dto.request.UserRegistrationRequest;

public abstract class UserRegistrationRequestValidator extends ControllerLayerValidator<UserRegistrationRequest> {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationRequest.class.equals(aClass);
    }

}
