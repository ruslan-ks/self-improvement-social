package rkostiuk.selfimprovement.util.validation.activity;

import rkostiuk.selfimprovement.dto.request.NewActivityRequest;
import rkostiuk.selfimprovement.util.i18n.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class NewPeriodicalLimitedCompletionsActivityRequestValidator implements NewSpecificTypeActivityRequestValidator {

    private final Translator translator;

    private final NewLimitedCompletionsActivityRequestValidator limitedCompletionsActivityValidator;

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
