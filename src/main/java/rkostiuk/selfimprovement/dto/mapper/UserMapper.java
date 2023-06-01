package rkostiuk.selfimprovement.dto.mapper;

import rkostiuk.selfimprovement.dto.request.UserRegistrationRequest;
import rkostiuk.selfimprovement.dto.response.DetailedUserResponse;
import rkostiuk.selfimprovement.dto.response.ShortUserResponse;
import rkostiuk.selfimprovement.entity.User;

public interface UserMapper {

    User toUser(UserRegistrationRequest userRegistrationRequest);

    DetailedUserResponse toDetailedUserResponse(User user);

    ShortUserResponse toShortUserResponse(User user);

}
