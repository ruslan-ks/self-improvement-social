package rkostiuk.selfimprovement.util.exception;

import rkostiuk.selfimprovement.util.validation.error.ValidationError;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public abstract class ValidationException extends RuntimeException {

    private final List<ValidationError> validationErrors = new ArrayList<>();

    protected ValidationException(List<ValidationError> validationErrors) {
        this.validationErrors.addAll(validationErrors);
    }

    @Override
    public String getMessage() {
        return "Validation failed: " + validationErrors;
    }

    public List<ValidationError> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

}
