package rkostiuk.selfimprovement.util.i18n;

import org.springframework.context.NoSuchMessageException;

public interface Translator {

    /**
     * @throws NoSuchMessageException if no corresponding message found
     */
    String translate(String messageCode);

    /**
     * Translate message and wrap along with its code
     * @throws NoSuchMessageException if no corresponding message found
     */
    UIMessage translateUIMessage(String messageCode);

}
