package rkostiuk.selfimprovement.controller.advice;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.util.HttpUtils;
import rkostiuk.selfimprovement.util.exception.FilterCriteriaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FilterCriteriaExceptionAdvice {

    @ExceptionHandler(FilterCriteriaException.class)
    public ResponseEntity<ResponseBody> handle(FilterCriteriaException ex) {
        return HttpUtils.badRequest(ex.getMessage());
    }

}
