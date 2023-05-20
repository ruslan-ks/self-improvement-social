package com.my.selfimprovement.util.validation.error;

import com.my.selfimprovement.util.i18n.ClientMessage;

/**
 * Error message dto used to provide client validation error message
 * @param value rejected value
 * @param clientMessage message explaining the cause
 * @param <T> either primitive or array type
 */
public record RejectedValueMessage<T>(T value, ClientMessage clientMessage) {}
