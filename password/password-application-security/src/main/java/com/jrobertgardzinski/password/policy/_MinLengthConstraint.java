package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

class _MinLengthConstraint extends ErrorConstraint<PlaintextPassword> {

    private final int minLength;

    _MinLengthConstraint(int minLength) {
        if (minLength < 5) {
            throw new IllegalArgumentException("minLength must be at least 5");
        }
        this.minLength = minLength;
    }

    @Override
    public boolean isSatisfied(PlaintextPassword candidate) {
        return candidate.value().length() >= minLength;
    }

    @Override
    public String code() {
        return "MIN_LENGTH_NOT_MET";
    }
}
