package rkostiuk.selfimprovement.util.validation.abstracts;

import rkostiuk.selfimprovement.util.exception.ControllerValidationException;
import rkostiuk.selfimprovement.util.exception.ValidationException;
import rkostiuk.selfimprovement.util.validation.error.ValidationError;

import java.util.List;

public abstract class ControllerLayerValidator<T> extends CustomValidator<T> {

    @Override
    protected ValidationException createValidationException(List<ValidationError> validationErrors) {
        return new ControllerValidationException(validationErrors);
    }

}
