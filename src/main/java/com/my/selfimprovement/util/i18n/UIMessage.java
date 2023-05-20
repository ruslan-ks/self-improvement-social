package com.my.selfimprovement.util.i18n;

/**
 * Contains message code and default message, so the client app can either provide its own translation
 * or use the default one.
 */
public record UIMessage(String messageCode, String defaultMessage) {}
