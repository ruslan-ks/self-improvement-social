package rkostiuk.selfimprovement.util.validation.activity;

import rkostiuk.selfimprovement.dto.request.NewActivityRequest;
import org.springframework.validation.Errors;

public interface NewSpecificTypeActivityRequestValidator {
    void validate(NewActivityRequest request, Errors errors);
}
