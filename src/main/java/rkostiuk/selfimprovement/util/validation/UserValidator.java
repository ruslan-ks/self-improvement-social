package rkostiuk.selfimprovement.util.validation;

import rkostiuk.selfimprovement.entity.User;
import rkostiuk.selfimprovement.util.validation.abstracts.ServiceLayerValidator;

public abstract class UserValidator extends ServiceLayerValidator<User> {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(User.class);
    }

}
