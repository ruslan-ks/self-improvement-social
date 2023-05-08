package com.my.selfimprovement.dto.request;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record UserUpdateRequest(
        @NotEmpty(message = "{valid.user.name.notEmpty}")
        @Size(min = 2, max = 128, message = "{valid.user.name.size}")
        String name,
        @Size(min = 2, max = 128, message = "{valid.user.surname.size}")
        String surname,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Past(message = "{valid.user.birthday.past}")
        Date birthday
) {}
