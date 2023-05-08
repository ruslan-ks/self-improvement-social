package com.my.selfimprovement.controller.advice;

import com.my.selfimprovement.dto.response.ResponseBody;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PropertyReferenceExceptionAdvice {

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ResponseBody> handlePropertyReferenceException(PropertyReferenceException ex) {
        ResponseBody responseBody = ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .developerMessage(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(responseBody);
    }

}
