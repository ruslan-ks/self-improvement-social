package com.my.selfimprovement.util.validation.abstracts;

import com.my.selfimprovement.util.exception.ServiceValidationException;
import com.my.selfimprovement.util.exception.ValidationException;
import com.my.selfimprovement.util.validation.error.ValidationError;

import java.util.List;

public abstract class ServiceLayerValidator<T> extends CustomValidator<T> {

    @Override
    protected ValidationException createValidationException(List<ValidationError> validationErrors) {
        return new ServiceValidationException(validationErrors);
    }

}
