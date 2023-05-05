package com.my.selfimprovement.util.exception;

public class FileRemovalException extends RuntimeException {

    public FileRemovalException() {
        super();
    }

    public FileRemovalException(String message) {
        super(message);
    }

    public FileRemovalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileRemovalException(Throwable cause) {
        super(cause);
    }
}
