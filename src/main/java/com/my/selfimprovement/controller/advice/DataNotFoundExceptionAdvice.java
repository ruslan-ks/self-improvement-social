package com.my.selfimprovement.controller.advice;

import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.util.HttpUtils;
import com.my.selfimprovement.util.exception.DataNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DataNotFoundExceptionAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseBody> handleDataNotFoundException(DataNotFoundException ex) {
        return HttpUtils.notFound(ex.getMessage());
    }

}
