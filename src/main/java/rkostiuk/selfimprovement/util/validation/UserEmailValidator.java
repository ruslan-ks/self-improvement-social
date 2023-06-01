package rkostiuk.selfimprovement.util.validation;

import org.springframework.validation.Errors;

public interface UserEmailValidator {

    /**
     * Validates user mail by checking its uniqueness. Adds error if email is already registered in the database.
     * @param email email to be validated
     * @param errors validation errors
     */
    void validate(String email, Errors errors);

}
