package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.decision.Decision;

import java.util.List;

public interface PasswordResetDecision extends Decision {

    static PasswordResetDecision allowed() {
        return new _PasswordResetAllowed();
    }

    static PasswordResetDecision rejected(List<ConstraintResult> violations) {
        return new _PasswordResetRejected(violations);
    }
}
