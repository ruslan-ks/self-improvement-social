package com.my.selfimprovement.util.validation.activity;

import com.my.selfimprovement.dto.request.ActivityType;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.util.validation.abstracts.ControllerLayerValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NewActivityRequestValidator extends ControllerLayerValidator<NewActivityRequest> {

    private final Map<ActivityType, NewSpecificTypeActivityRequestValidator> validatorMap;

    @Override
    public boolean supports(Class<?> aClass) {
        return NewActivityRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var request = (NewActivityRequest) target;
        NewSpecificTypeActivityRequestValidator validator = getValidatorForType(request.getType());
        validator.validate(request, errors);
    }

    private NewSpecificTypeActivityRequestValidator getValidatorForType(ActivityType type) {
        return validatorMap.get(type);
    }

}
