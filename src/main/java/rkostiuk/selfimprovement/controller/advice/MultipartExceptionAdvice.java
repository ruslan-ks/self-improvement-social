package rkostiuk.selfimprovement.controller.advice;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
@Slf4j
public class MultipartExceptionAdvice {

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBody handleMultipartException(MultipartException ex) {
        log.warn("MultipartException caught: ", ex);
        return ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .developerMessage(ex.getMessage())
                .build();
    }

}
