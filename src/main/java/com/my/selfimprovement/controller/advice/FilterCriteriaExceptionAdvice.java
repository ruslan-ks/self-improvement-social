package com.my.selfimprovement.controller.advice;

import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.exception.FilterCriteriaException;
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
