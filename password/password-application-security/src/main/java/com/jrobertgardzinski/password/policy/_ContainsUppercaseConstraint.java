package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.regex.Pattern;

class _ContainsUppercaseConstraint extends ErrorConstraint<PlaintextPassword> {

    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");

    @Override
    public boolean isSatisfied(PlaintextPassword candidate) {
        return UPPERCASE.matcher(candidate.value()).find();
    }

    @Override
    public String code() {
        return "UPPERCASE_REQUIRED";
    }
}
