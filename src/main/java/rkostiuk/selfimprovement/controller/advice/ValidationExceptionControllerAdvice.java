package rkostiuk.selfimprovement.controller.advice;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.dto.response.ValidationErrorUIMessage;
import rkostiuk.selfimprovement.util.exception.ControllerValidationException;
import rkostiuk.selfimprovement.util.i18n.Translator;
import rkostiuk.selfimprovement.util.i18n.UIMessage;
import rkostiuk.selfimprovement.util.validation.error.ValidationError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

/**
 * Handles exceptions thrown by <strong>controller level validators</strong> and returns corresponding response.
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ValidationExceptionControllerAdvice {

    private final Translator translator;

    /**
     * Handles exceptions thrown by <strong>Spring</strong> validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBody handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ValidationErrorUIMessage> validationErrorUIMessages = ex.getFieldErrors().stream()
                .map(ValidationExceptionControllerAdvice::toValidationErrorUIMessage)
                .toList();
        logValidationErrors(validationErrorUIMessages);
        return validationErrorResponseBody(validationErrorUIMessages);
    }

    private static ValidationErrorUIMessage toValidationErrorUIMessage(FieldError fieldError) {
        var uiMessage = new UIMessage(fieldError.getCode(), fieldError.getDefaultMessage());
        return new ValidationErrorUIMessage(fieldError.getField(), fieldError.getRejectedValue(), uiMessage);
    }

    /**
     * Handles exceptions thrown by <strong>custom</strong> controller level validators
     */
    @ExceptionHandler(ControllerValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBody handleConstraintViolation(ControllerValidationException ex) {
        List<ValidationErrorUIMessage> validationErrorUIMessages = ex.getValidationErrors().stream()
                .map(this::toValidationErrorUIMessage)
                .toList();
        logValidationErrors(validationErrorUIMessages);
        return validationErrorResponseBody(validationErrorUIMessages);
    }

    private ValidationErrorUIMessage toValidationErrorUIMessage(ValidationError validationError) {
        String field = validationError.field();
        Object rejectedValue = validationError.rejectedValue().value();
        String errorCode = validationError.rejectedValue().errorCode();
        String errorMessage = translator.translate(errorCode);
        return new ValidationErrorUIMessage(field, rejectedValue, new UIMessage(errorCode, errorMessage));
    }

    private void logValidationErrors(List<ValidationErrorUIMessage> validationErrors) {
        for (var error : validationErrors) {
            log.warn("Invalid field value: {}: {} - {}", error.field(), error.rejectedValue(),
                    error.uiMessage().messageCode());
        }
    }

    private ResponseBody validationErrorResponseBody(List<ValidationErrorUIMessage> validationErrorUIMessages) {
        UIMessage validationFailureMsg = new UIMessage("validation.fail",
                translator.translate("validation.fail"));
        return ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(validationFailureMsg)
                .data(Map.of("validationErrors", validationErrorUIMessages))
                .build();
    }

}
