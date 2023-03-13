package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.util.exception.ControllerValidationException;
import com.my.selfimprovement.util.exception.ValidationException;

import java.util.Map;

public abstract class ControllerLayerValidator<T> extends CustomValidator<T> {

    @Override
    protected final ValidationException createValidationException(Map<String, String> fieldErrorMap) {
        return new ControllerValidationException(fieldErrorMap);
    }

}
