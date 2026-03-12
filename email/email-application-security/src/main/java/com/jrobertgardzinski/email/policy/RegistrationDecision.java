package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.ConstraintResult;
import com.jrobertgardzinski.util.Decision;
import java.util.List;

public record RegistrationDecision(Decision decision, List<ConstraintResult> violations) {

    public boolean isAllowed() {
        return decision == Decision.ALLOWED;
    }

    public boolean isRejected() {
        return decision == Decision.REJECTED;
    }
}
