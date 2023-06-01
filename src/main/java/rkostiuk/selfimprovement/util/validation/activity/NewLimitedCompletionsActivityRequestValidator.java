package rkostiuk.selfimprovement.util.validation.activity;

import rkostiuk.selfimprovement.dto.request.NewActivityRequest;
import rkostiuk.selfimprovement.util.i18n.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class NewLimitedCompletionsActivityRequestValidator implements NewSpecificTypeActivityRequestValidator {

    private static final String LIMIT_NOT_NULL = "valid.limitedCompletionsActivity.completionsLimit.notNull";

    private final Translator translator;

    @Override
    public void validate(NewActivityRequest request, Errors errors) {
        Integer completionsLimit = request.getCompletionsLimit();
        if (completionsLimit == null) {
            String message = translator.translate(LIMIT_NOT_NULL);
            errors.rejectValue("completionsLimit", LIMIT_NOT_NULL, message);
        }
    }

}
