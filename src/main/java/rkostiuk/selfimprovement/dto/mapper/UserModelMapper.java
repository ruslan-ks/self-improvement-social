package rkostiuk.selfimprovement.dto.mapper;

import rkostiuk.selfimprovement.dto.request.UserRegistrationRequest;
import rkostiuk.selfimprovement.dto.response.DetailedUserResponse;
import rkostiuk.selfimprovement.dto.response.ShortUserResponse;
import rkostiuk.selfimprovement.entity.User;
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
