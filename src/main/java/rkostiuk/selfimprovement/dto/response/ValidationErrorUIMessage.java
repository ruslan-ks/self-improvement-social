package rkostiuk.selfimprovement.dto.response;

import rkostiuk.selfimprovement.util.i18n.UIMessage;

public record ValidationErrorUIMessage(String field, Object rejectedValue, UIMessage uiMessage) {}
