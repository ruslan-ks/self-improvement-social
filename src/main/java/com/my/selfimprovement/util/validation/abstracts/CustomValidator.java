package com.my.selfimprovement.util.validation.abstracts;

import com.my.selfimprovement.util.exception.ValidationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * Adds throwing {@link CustomValidator#validate(T obj)} method.<br>
 * Validator class should implement this interface if it needs single-argument throwing alternative
 * for {@link Validator#validate(Object, Errors)} method
 * @param <T> validated object type
 */
public abstract class CustomValidator<T> implements Validator {

    /**
     * Throws {@link ValidationException} implementation(implementation-defined) if any of {@code obj} fields are
     * invalid
     * @param obj object to be validated
     */
    public void validate(T obj) {
        var bindingResult = new BeanPropertyBindingResult(obj, obj.getClass().getSimpleName());
        validate(obj, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrorMap = bindingResult.getFieldErrors()
                    .stream()
                    .collect(HashMap::new, (m, v) -> m.put(v.getField(), v.getDefaultMessage()), HashMap::putAll);
            throw createValidationException(fieldErrorMap);
        }
    }

    /**
     * Creates ValidationException implementation instance to be thrown. Should always return new instance.<br>
     * Called only when validation errors occur in {@link CustomValidator#validate(Object)}
     * @param fieldErrorMap validation errors
     * @return new instance of ValidationException to be throws from {@link CustomValidator#validate(Object)}
     */
    protected abstract ValidationException createValidationException(Map<String, String> fieldErrorMap);

}
