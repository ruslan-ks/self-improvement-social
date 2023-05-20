package com.my.selfimprovement.util.validation.error;

import java.io.Serializable;

public record ValidationError(String field, RejectedValue<?> rejectedValue) implements Serializable {}
