package com.my.selfimprovement.util.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageSourceTranslator implements Translator {

    private final MessageSource messageSource;

    @Override
    public String translate(String messageCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageCode, null, locale);
    }

    @Override
    public UIMessage translateUIMessage(String messageCode) {
        return new UIMessage(messageCode, translate(messageCode));
    }

}
