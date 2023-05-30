package com.my.selfimprovement.dto.response;

import com.my.selfimprovement.entity.User;
import lombok.Data;

import java.util.Date;

@Data
public class ShortUserResponse {
    private long id;
    private String name;
    private String surname;
    private Date birthday;
    private long activityCount;
    private User.Status status;
    private User.Role role;
}
