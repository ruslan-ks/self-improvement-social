package com.my.selfimprovement.util.validation.activity;

import com.my.selfimprovement.dto.request.NewActivityRequest;
import org.springframework.validation.Errors;

public interface NewSpecificTypeActivityRequestValidator {
    void validate(NewActivityRequest request, Errors errors);
}
