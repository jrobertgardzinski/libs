package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.regex.Pattern;

class _RfcFormatConstraint extends ErrorConstraint<Email> {

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
    public String code() {
        return "RFC_FORMAT_INVALID";
    }
}
