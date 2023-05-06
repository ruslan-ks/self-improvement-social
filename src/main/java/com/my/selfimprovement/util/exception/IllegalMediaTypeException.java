package com.my.selfimprovement.util.exception;

public class IllegalMediaTypeException extends RuntimeException {

    public IllegalMediaTypeException() {
        super();
    }

    public IllegalMediaTypeException(String message) {
        super(message);
    }

    public IllegalMediaTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalMediaTypeException(Throwable cause) {
        super(cause);
    }

}
