package com.my.selfimprovement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody {

    @Builder
    public ResponseBody(Instant timestamp, HttpStatus status, String message, String developerMessage,
                        Map<?, ?> data) {
        this.timestamp = Optional.ofNullable(timestamp).orElseGet(Instant::now);
        this.status = status;
        this.message = message;
        this.developerMessage = developerMessage;
        this.data = data;
        this.statusCode = status.value();
    }

    protected Instant timestamp;
    protected HttpStatus status;
    protected int statusCode;
    protected String message;
    protected String developerMessage;
    protected Map<?, ?> data;

}
