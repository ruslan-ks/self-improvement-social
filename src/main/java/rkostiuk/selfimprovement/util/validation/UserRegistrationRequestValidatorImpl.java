package rkostiuk.selfimprovement.util.validation;

import rkostiuk.selfimprovement.dto.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class UserRegistrationRequestValidatorImpl extends UserRegistrationRequestValidator {

    private static final String IDENTICAL_PASSWORDS_KEY = "valid.user.passwords.identical";

    private final UserEmailValidator userEmailValidator;

    private final MessageSource messageSource;

    @Override
    public void validate(Object target, Errors errors) {
        var userRegistrationRequest = (UserRegistrationRequest) target;
        userEmailValidator.validate(userRegistrationRequest.getEmail(), errors);
        validatePasswords(userRegistrationRequest, errors);
    }

    private void validatePasswords(UserRegistrationRequest request, Errors errors) {
        String password = request.getPassword();
        String repeatedPassword = request.getRepeatedPassword();
        if (password != null && !password.equals(repeatedPassword)) {
            String message = messageSource.getMessage(IDENTICAL_PASSWORDS_KEY, null,
                    LocaleContextHolder.getLocale());
            errors.rejectValue("password", IDENTICAL_PASSWORDS_KEY, message);
            errors.rejectValue("repeatedPassword", IDENTICAL_PASSWORDS_KEY, message);
        }
    }

}
