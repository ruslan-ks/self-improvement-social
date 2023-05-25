package com.my.selfimprovement.util.exception;

public class UserActivityNotFoundException extends DataNotFoundException {

    public UserActivityNotFoundException() {
        super();
    }

    public UserActivityNotFoundException(String message) {
        super(message);
    }

    public UserActivityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserActivityNotFoundException(Throwable cause) {
        super(cause);
    }

}
