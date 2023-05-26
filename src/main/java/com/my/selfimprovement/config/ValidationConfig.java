package com.my.selfimprovement.config;

import com.my.selfimprovement.dto.request.ActivityType;
import com.my.selfimprovement.util.validation.activity.LimitedCompletionsActivityValidator;
import com.my.selfimprovement.util.validation.activity.NewSpecificTypeActivityRequestValidator;
import com.my.selfimprovement.util.validation.activity.PeriodicalLimitedCompletionsActivityValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ValidationConfig {

    @Bean
    public Map<ActivityType, NewSpecificTypeActivityRequestValidator> newActivityRequestValidatorMap(
            LimitedCompletionsActivityValidator limitedCompletionsActivityValidator,
            PeriodicalLimitedCompletionsActivityValidator periodicalLimitedCompletionsActivityValidator) {
        return Map.of(ActivityType.LIMITED_COMPLETIONS,
                limitedCompletionsActivityValidator,
                ActivityType.PERIODICAL_LIMITED_COMPLETIONS,
                periodicalLimitedCompletionsActivityValidator);
    }

}
