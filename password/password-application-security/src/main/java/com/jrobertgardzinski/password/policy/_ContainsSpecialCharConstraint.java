package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.regex.Pattern;

class _ContainsSpecialCharConstraint extends ErrorConstraint<PlaintextPassword> {

    private final String specialChars;
    private final Pattern pattern;

    _ContainsSpecialCharConstraint(String specialChars) {
        this.specialChars = specialChars;
        this.pattern = Pattern.compile("[" + Pattern.quote(specialChars) + "]");
    }

    @Override
    public boolean isSatisfied(PlaintextPassword candidate) {
        return pattern.matcher(candidate.value()).find();
    }

    @Override
    public String code() {
        return "SPECIAL_CHAR_REQUIRED";
    }
}
