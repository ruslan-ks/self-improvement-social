package com.my.selfimprovement.util.validation.activity;

import com.my.selfimprovement.dto.request.ActivityType;
import com.my.selfimprovement.dto.request.NewActivityRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MapBasedNewSpecificTypeActivityRequestValidatorResolver
        implements NewSpecificTypeActivityRequestValidatorResolver {

    private final Map<ActivityType, NewSpecificTypeActivityRequestValidator> validatorMap;

    private final NoOpNewSpecificTypeActivityRequestValidator noOpValidator;

    public MapBasedNewSpecificTypeActivityRequestValidatorResolver(
            NewLimitedCompletionsActivityRequestValidator limitedCompletionsValidator,
            NewPeriodicalLimitedCompletionsActivityRequestValidator periodicalLimitedCompletionsValidator,
            NoOpNewSpecificTypeActivityRequestValidator noOpValidator) {
        this.validatorMap = Map.of(ActivityType.LIMITED_COMPLETIONS, limitedCompletionsValidator,
                ActivityType.PERIODICAL_LIMITED_COMPLETIONS, periodicalLimitedCompletionsValidator);
        this.noOpValidator = noOpValidator;
    }

    @Override
    public NewSpecificTypeActivityRequestValidator getValidatorFor(NewActivityRequest request) {
        return validatorMap.getOrDefault(request.getType(), noOpValidator);
    }

}
