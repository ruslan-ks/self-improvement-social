package rkostiuk.selfimprovement.util.validation.activity;

import rkostiuk.selfimprovement.dto.request.NewActivityRequest;

public interface NewSpecificTypeActivityRequestValidatorResolver {
    /**
     * Returns validator based on {@code request.type}
     * @param request to be validated by the validator
     * @return validator to validate the request if one exists,
     * otherwise returns {@link NoOpNewSpecificTypeActivityRequestValidator}
     */
    NewSpecificTypeActivityRequestValidator getValidatorFor(NewActivityRequest request);
}
