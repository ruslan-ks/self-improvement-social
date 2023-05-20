package com.my.selfimprovement.util.i18n;

import org.springframework.context.NoSuchMessageException;

public interface Translator {
    /**
     * @throws NoSuchMessageException if no corresponding message found
     */
    String translate(String messageCode);
}
