package com.my.selfimprovement.util.validation.activity;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.util.validation.abstracts.ControllerLayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class NewActivityRequestValidator extends ControllerLayerValidator<NewActivityRequest> {

    private final NewSpecificTypeActivityRequestValidatorResolver validatorResolver;

    @Override
    public boolean supports(Class<?> aClass) {
        return NewActivityRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var request = (NewActivityRequest) target;
        NewSpecificTypeActivityRequestValidator validator = validatorResolver.getValidatorFor(request);
        validator.validate(request, errors);
    }

}
