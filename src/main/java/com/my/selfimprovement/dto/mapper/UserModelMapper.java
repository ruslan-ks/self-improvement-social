package com.my.selfimprovement.dto.mapper;

import com.my.selfimprovement.dto.request.UserRegistrationRequest;
import com.my.selfimprovement.dto.response.DetailedUserResponse;
import com.my.selfimprovement.dto.response.ShortUserResponse;
import com.my.selfimprovement.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModelMapper implements UserMapper {

    private final ModelMapper modelMapper;

    @Override
    public User toUser(UserRegistrationRequest userRegistrationRequest) {
        return modelMapper.map(userRegistrationRequest, User.class);
    }

    @Override
    public DetailedUserResponse toDetailedUserResponse(User user) {
        return modelMapper.map(user, DetailedUserResponse.class);
    }

    @Override
    public ShortUserResponse toShortUserResponse(User user) {
        ShortUserResponse shortUserResponse = modelMapper.map(user, ShortUserResponse.class);
        shortUserResponse.setActivityCount(user.getActivities().size());
        return shortUserResponse;
    }

}
