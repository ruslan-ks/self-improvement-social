package com.my.selfimprovement.config;

import com.my.selfimprovement.util.exception.ControllerValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ValidatingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getFieldErrors()
                .stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getField(), v.getDefaultMessage()), HashMap::putAll);
        logValidationErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(ControllerValidationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ControllerValidationException ex) {
        Map<String, String> errors = ex.getFieldErrorMap();
        logValidationErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    private void logValidationErrors(Map<String, String> fieldErrorMap) {
        for (var entry : fieldErrorMap.entrySet()) {
            log.warn("Invalid field value: {}: {}", entry.getKey(), entry.getValue());
        }
    }

}
