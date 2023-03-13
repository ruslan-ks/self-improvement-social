package com.my.selfimprovement.util.exception;

import java.util.Collections;
import java.util.Map;

public abstract class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrorMap;

    protected ValidationException(Map<String, String> fieldErrorMap) {
        this.fieldErrorMap = fieldErrorMap;
    }

    @Override
    public String getMessage() {
        return "Validation failed: " + fieldErrorMap;
    }

    public Map<String, String> getFieldErrorMap() {
        return Collections.unmodifiableMap(fieldErrorMap);
    }

}
