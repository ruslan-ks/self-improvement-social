package rkostiuk.selfimprovement.util.validation.activity;

import rkostiuk.selfimprovement.dto.request.NewActivityRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class NoOpNewSpecificTypeActivityRequestValidator implements NewSpecificTypeActivityRequestValidator {

    @Override
    public void validate(NewActivityRequest request, Errors errors) {
        // Do nothing
    }

}
