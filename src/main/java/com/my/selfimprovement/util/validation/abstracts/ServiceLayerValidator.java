package com.my.selfimprovement.util.validation.abstracts;

import com.my.selfimprovement.util.exception.ServiceValidationException;
import com.my.selfimprovement.util.exception.ValidationException;

import java.util.Map;

public abstract class ServiceLayerValidator<T> extends CustomValidator<T> {

    @Override
    protected final ValidationException createValidationException(Map<String, String> fieldErrorMap) {
        return new ServiceValidationException(fieldErrorMap);
    }

}
