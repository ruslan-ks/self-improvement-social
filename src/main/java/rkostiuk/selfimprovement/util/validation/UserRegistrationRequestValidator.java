package rkostiuk.selfimprovement.util.validation;

import rkostiuk.selfimprovement.dto.request.UserRegistrationRequest;
import rkostiuk.selfimprovement.util.validation.abstracts.ControllerLayerValidator;

public abstract class UserRegistrationRequestValidator extends ControllerLayerValidator<UserRegistrationRequest> {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationRequest.class.equals(aClass);
    }

}
