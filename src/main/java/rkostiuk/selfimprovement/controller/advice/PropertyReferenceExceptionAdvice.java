package rkostiuk.selfimprovement.controller.advice;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PropertyReferenceExceptionAdvice {

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBody handlePropertyReferenceException(PropertyReferenceException ex) {
        return ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .developerMessage(ex.getMessage())
                .build();
    }

}
