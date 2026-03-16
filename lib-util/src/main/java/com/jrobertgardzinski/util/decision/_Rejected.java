package com.jrobertgardzinski.util.decision;

import com.jrobertgardzinski.util.constraint.ConstraintResult;

import java.util.List;

record _Rejected(List<ConstraintResult> violations) implements Decision {

    public boolean isAllowed() { return false; }
    public boolean isRejected() { return true; }
}
