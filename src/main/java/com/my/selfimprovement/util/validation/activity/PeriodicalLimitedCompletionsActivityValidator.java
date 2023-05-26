package com.my.selfimprovement.util.validation.activity;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import com.my.selfimprovement.util.i18n.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class PeriodicalLimitedCompletionsActivityValidator implements NewSpecificTypeActivityRequestValidator {

    private final Translator translator;

    private final LimitedCompletionsActivityValidator limitedCompletionsActivityValidator;

    private static final String DURATION_NOT_NULL = "valid.limitedCompletionsActivity.periodDurationMinutes.notNull";

    @Override
    public void validate(NewActivityRequest request, Errors errors) {
        Long periodDurationMinutes = request.getPeriodDurationMinutes();
        if (periodDurationMinutes == null) {
            String message = translator.translate(DURATION_NOT_NULL);
            errors.rejectValue("periodDurationMinutes", DURATION_NOT_NULL, message);
        }
        limitedCompletionsActivityValidator.validate(request, errors);
    }

}
