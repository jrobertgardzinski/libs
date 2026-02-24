package com.jrobertgardzinski.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;

import java.util.regex.Pattern;

class _RfcFormatConstraint extends Constraint<Email> {

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
