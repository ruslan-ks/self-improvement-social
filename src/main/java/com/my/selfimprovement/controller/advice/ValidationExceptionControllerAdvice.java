package com.my.selfimprovement.controller.advice;

import com.my.selfimprovement.dto.ResponseBody;
import com.my.selfimprovement.util.exception.ControllerValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles exceptions thrown by <strong>controller level validators</strong> and returns corresponding response.
 */
@ControllerAdvice
@Slf4j
public class ValidationExceptionControllerAdvice {

    /**
     * Handles exceptions thrown by <strong>Spring</strong> validation
     * @param ex caught exception
     * @return response with data - map of field:error pairs
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBody> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getFieldErrors()
                .stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getField(), v.getDefaultMessage()), HashMap::putAll);
        logValidationErrors(errors);
        ResponseBody responseBody = validationFailResponseBody(errors);
        return ResponseEntity.badRequest().body(responseBody);
    }

    /**
     * Handles exceptions thrown by <strong>custom</strong> controller level validators
     * @param ex caught exception
     * @return response - map of field:error pairs
     */
    @ExceptionHandler(ControllerValidationException.class)
    public ResponseEntity<ResponseBody> handleConstraintViolation(ControllerValidationException ex) {
        Map<String, String> errors = ex.getFieldErrorMap();
        logValidationErrors(errors);
        ResponseBody responseBody = validationFailResponseBody(errors);
        return ResponseEntity.badRequest().body(responseBody);
    }

    private void logValidationErrors(Map<String, String> fieldErrorMap) {
        for (var entry : fieldErrorMap.entrySet()) {
            log.warn("Invalid field value: {}: {}", entry.getKey(), entry.getValue());
        }
    }

    private ResponseBody validationFailResponseBody(Map<String, String> errors) {
        return ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Validation failed")
                .data(Map.of("errors", errors))
                .build();
    }

}
