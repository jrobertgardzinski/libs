package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;

import java.util.List;

record _RegistrationRejected(List<ConstraintResult> violations) implements RegistrationDecision {

    public boolean isAllowed() { return false; }
    public boolean isRejected() { return true; }
}
