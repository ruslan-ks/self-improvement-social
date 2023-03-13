package com.my.selfimprovement.util.validation;

import com.my.selfimprovement.entity.User;

public abstract class UserValidator extends ServiceLayerValidator<User> {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(User.class);
    }

}
