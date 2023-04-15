package com.my.selfimprovement.dto.response;

import com.my.selfimprovement.entity.User;
import lombok.Data;

import java.time.Instant;
import java.util.*;

@Data
public class DetailedUserResponse {

    private long id;

    private String name;

    private String surname;

    private String email;

    private Date birthday;

    private User.Role role = User.Role.USER;

    private User.Status status = User.Status.ACTIVE;

    private Instant registeredAt;

}
