package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.regex.Pattern;

class _ContainsLowercaseConstraint extends ErrorConstraint<PlaintextPassword> {

    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");

    @Override
    public boolean isSatisfied(PlaintextPassword candidate) {
        return LOWERCASE.matcher(candidate.value()).find();
    }

    @Override
    public String code() {
        return "LOWERCASE_REQUIRED";
    }
}
