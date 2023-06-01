package rkostiuk.selfimprovement.util.validation.error;

import java.io.Serializable;

/**
 * Represents rejected value and error code used to provide client validation error message
 * @param value rejected value
 * @param errorCode error message code, that allows to get message from messages resource bundle
 * @param <T> either primitive or array type
 */
public record RejectedValue<T>(T value, String errorCode) implements Serializable {}
