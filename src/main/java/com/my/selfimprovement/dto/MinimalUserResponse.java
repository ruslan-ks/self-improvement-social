package com.my.selfimprovement.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MinimalUserResponse {

    private long id;

    private String name;

    private String surname;

    private Date birthday;

    private long activityCount;

}
