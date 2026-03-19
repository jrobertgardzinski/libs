package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.regex.Pattern;

class _ContainsDigitConstraint extends ErrorConstraint<PlaintextPassword> {

    private static final Pattern DIGIT = Pattern.compile("\\d");

    @Override
    public boolean isSatisfied(PlaintextPassword candidate) {
        return DIGIT.matcher(candidate.value()).find();
    }

    @Override
    public String code() {
        return "DIGIT_REQUIRED";
    }
}
