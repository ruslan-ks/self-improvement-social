package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.dto.request.NewCategoryRequest;
import com.my.selfimprovement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class ServiceBasedNewCategoryValidator extends NewCategoryValidator {

    private final CategoryService categoryService;

    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> aClass) {
        return NewCategoryRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var newCategoryRequest = (NewCategoryRequest) target;
        String name = newCategoryRequest.name();
        if (categoryService.getByName(name).isPresent()) {
            String message = messageSource.getMessage("valid.category.name.unique", null,
                    LocaleContextHolder.getLocale());
            errors.rejectValue("name", "valid.category.name.unique", message);
        }
    }
}
