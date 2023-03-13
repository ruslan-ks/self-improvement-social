package com.my.selfimprovement.util.exception;

import java.util.Map;

/**
 * Should be used when validating the service layer input.<br>
 * This exception should be thrown by service layer validators. This exception indicates a bug.<br>
 * <strong>Should never be handled by a controller's exception handler.</strong>
 */
public class ServiceValidationException extends ValidationException {

    public ServiceValidationException(Map<String, String> fieldErrorMap) {
        super(fieldErrorMap);
    }

}
