package rkostiuk.selfimprovement.controller.advice;

import rkostiuk.selfimprovement.dto.response.ResponseBody;
import rkostiuk.selfimprovement.util.exception.IllegalMediaTypeException;
import rkostiuk.selfimprovement.util.i18n.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class MediaTypeExceptionAdvice {

    private final Translator translator;

    @ExceptionHandler({IllegalMediaTypeException.class, InvalidMediaTypeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBody handeIllegalMediaTypeException(RuntimeException ex) {
        var uiMessage = translator.translateUIMessage("file.formatNotSupported");
        return ResponseBody.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(uiMessage)
                .developerMessage(ex.getMessage())
                .build();
    }

}
