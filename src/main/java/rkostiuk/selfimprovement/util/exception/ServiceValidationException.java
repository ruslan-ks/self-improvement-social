package rkostiuk.selfimprovement.util.exception;

import rkostiuk.selfimprovement.util.validation.error.ValidationError;

import java.util.List;

/**
 * Should be used when validating the service layer input.<br>
 * This exception should be thrown by service layer validators. This exception indicates a bug.<br>
 * <strong>Should never be handled by a controller's exception handler.</strong>
 */
public class ServiceValidationException extends ValidationException {

    public ServiceValidationException(List<ValidationError> validationErrors) {
        super(validationErrors);
    }

}
