package com.my.selfimprovement.dto.response;

import com.my.selfimprovement.util.i18n.UIMessage;

public record ValidationErrorUIMessage(String field, Object rejectedValue, UIMessage uiMessage) {}
