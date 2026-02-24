package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

import java.util.regex.Pattern;

public class RfcFormatConstraint extends EmailConstraint {

    private static final Pattern RFC_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
            "@" +
            "[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
            "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    @Override
    public boolean isSatisfied(Email candidate) {
        return RFC_PATTERN.matcher(candidate.value()).matches();
    }

    @Override
    public String errorMessage() {
        return "email does not meet the RFC regex";
    }
}
