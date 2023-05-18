package com.my.selfimprovement.util.exception;

public class AvatarNotFoundException extends DataNotFoundException {

    public AvatarNotFoundException() {
        super();
    }

    public AvatarNotFoundException(String message) {
        super(message);
    }

    public AvatarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AvatarNotFoundException(Throwable cause) {
        super(cause);
    }

}
