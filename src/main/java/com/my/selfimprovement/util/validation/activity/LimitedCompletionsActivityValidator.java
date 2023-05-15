package com.my.selfimprovement.util.validation.activity;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class LimitedCompletionsActivityValidator implements NewSpecificTypeActivityRequestValidator {

    private static final String LIMIT_FIELD_NAME = "completionsLimit";

    private static final String LIMIT_NOT_NULL = "valid.limitedCompletionsActivity.completionsLimit.notNull";
    private static final String LIMIT_VALUE_RANGE = "valid.limitedCompletionsActivity.completionsLimit";

    private final MessageSource messageSource;

    @Override
    public void validate(NewActivityRequest request, Errors errors) {
        Integer completionsLimit = request.getCompletionsLimit();
        if (completionsLimit == null) {
            String message = messageSource.getMessage(LIMIT_NOT_NULL, null, LocaleContextHolder.getLocale());
            errors.rejectValue(LIMIT_FIELD_NAME, LIMIT_NOT_NULL, message);
            return;
        }
        if (completionsLimit < 1) {
            String message = messageSource.getMessage(LIMIT_VALUE_RANGE, null, LocaleContextHolder.getLocale());
            errors.rejectValue(LIMIT_FIELD_NAME, LIMIT_VALUE_RANGE, message);
        }
    }

}
