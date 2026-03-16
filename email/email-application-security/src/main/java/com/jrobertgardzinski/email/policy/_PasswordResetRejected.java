package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;

import java.util.List;

record _PasswordResetRejected(List<ConstraintResult> violations) implements PasswordResetDecision {

    public boolean isAllowed() { return false; }
    public boolean isRejected() { return true; }
}
