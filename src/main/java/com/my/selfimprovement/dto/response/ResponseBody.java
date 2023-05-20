package com.my.selfimprovement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.selfimprovement.util.i18n.UIMessage;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody {

    protected Instant timestamp;
    protected HttpStatus status;
    protected int statusCode;
    protected UIMessage message;
    protected String developerMessage;
    protected Map<String, ?> data;

    @Builder
    public ResponseBody(Instant timestamp, HttpStatus status, UIMessage message, String developerMessage,
                        Map<String, ?> data) {
        this.timestamp = Optional.ofNullable(timestamp).orElseGet(Instant::now);
        this.status = status;
        this.message = message;
        this.developerMessage = developerMessage;
        this.data = data;
        this.statusCode = status.value();
    }

    public static ResponseBody of(HttpStatus status, Map<String, ?> data) {
        return ResponseBody.builder()
                .status(status)
                .data(data)
                .build();
    }

    public static <T> ResponseBody of(HttpStatus status, String key, T value) {
        return of(status, Map.of(key, value));
    }

    public static ResponseBody ok(Map<String, ?> data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> ResponseBody ok(String key, T value) {
        return ok(Map.of(key, value));
    }

}
