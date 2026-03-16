package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.Severity;

import java.util.List;
import java.util.Optional;

class _FastPhase<A> {

    private final List<ErrorConstraint<A>> constraints;

    _FastPhase(List<ErrorConstraint<A>> constraints) {
        this.constraints = constraints;
    }

    Optional<ConstraintResult> evaluate(A input) {
        for (var c : constraints) {
            if (!c.isSatisfied(input)) {
                return Optional.of(new ConstraintResult(Severity.BLOCKING, c.code()));
            }
        }
        return Optional.empty();
    }
}
