package com.my.selfimprovement.config;

import com.my.selfimprovement.dto.request.ActivityType;
import com.my.selfimprovement.util.validation.activity.LimitedCompletionsActivityValidator;
import com.my.selfimprovement.util.validation.activity.NewSpecificTypeActivityRequestValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ValidationConfig {

    @Bean
    public Map<ActivityType, NewSpecificTypeActivityRequestValidator> newActivityRequestValidatorMap(MessageSource ms) {
        var limitedCompletionsActivityValidator = new LimitedCompletionsActivityValidator(ms);

        // Use the same validator both for LIMITED_COMPLETIONS and PERIODICAL_LIMITED_COMPLETIONS,
        // since PERIODICAL_LIMITED_COMPLETIONS represents a subtype and doesn't require new specific validation steps
        return Map.of(ActivityType.LIMITED_COMPLETIONS, limitedCompletionsActivityValidator,
                ActivityType.PERIODICAL_LIMITED_COMPLETIONS, limitedCompletionsActivityValidator);
    }

}
