package com.my.selfimprovement.controller.advice;

import com.my.selfimprovement.dto.response.ResponseBody;
import com.my.selfimprovement.util.exception.IllegalMediaTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MediaTypeExceptionAdvice {

    @ExceptionHandler({IllegalMediaTypeException.class, InvalidMediaTypeException.class})
    public ResponseEntity<ResponseBody> handeIllegalMediaTypeException(RuntimeException ex) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(responseBody);
    }

}