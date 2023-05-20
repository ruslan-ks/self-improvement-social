package com.my.selfimprovement.util.exception;

import com.my.selfimprovement.util.validation.error.ValidationError;

import java.util.List;

/**
 * Should be used when validating the Controller input.<br>
 * This exception should be thrown by controller level validators.<br>
 * <strong>Should be handled by a controller's exception handler.</strong>
 */
public class ControllerValidationException extends ValidationException {

    public ControllerValidationException(List<ValidationError> validationErrors) {
        super(validationErrors);
    }

}
