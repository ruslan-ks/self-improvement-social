package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.dto.response.DetailedUserResponse;
import com.my.selfimprovement.dto.response.ShortUserResponse;
import com.my.selfimprovement.entity.User;

public interface UserMapper {

    User toUser(UserRegistrationRequest userRegistrationRequest);

    DetailedUserResponse toDetailedUserResponse(User user);

    ShortUserResponse toShortUserResponse(User user);

}
