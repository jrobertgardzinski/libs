package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.decision.Decision;

import java.util.List;

public interface RegistrationDecision extends Decision {

    static RegistrationDecision allowed() {
        return new _RegistrationAllowed();
    }

    static RegistrationDecision rejected(List<ConstraintResult> violations) {
        return new _RegistrationRejected(violations);
    }
}
