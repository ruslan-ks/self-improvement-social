package com.my.selfimprovement.util.validation;

import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Extends {@link Validator} by adding throwing {@link CustomValidator#validate(T obj)} method.<br>
 * Validator class should implement this interface if it needs single-argument throwing alternative
 * for {@link Validator#validate(Object, Errors)} method
 * @param <T> validated object type
 */
public interface CustomValidator<T> extends Validator {

    /**
     * Throws {@link ConstraintViolationException} if any of {@code obj} fields are invalid
     * @param obj object to be validated
     */
    default void validate(T obj) {
        var builder = new StringBuilder();
        var bindingResult = new BeanPropertyBindingResult(obj, obj.getClass().getSimpleName());
        validate(obj, bindingResult);
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors()
                    .forEach(err -> builder.append(err.getField())
                            .append(": ")
                            .append(err.getRejectedValue())
                            .append(" - ")
                            .append(err.getDefaultMessage()));
            throw new ConstraintViolationException(builder.toString(), null);
        }
    }

}
