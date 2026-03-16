package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.constraint.Severity;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

import java.util.List;
import java.util.function.Consumer;

class _WarningPhase<A> {

    private final List<WarningConstraint<A>> constraints;

    _WarningPhase(List<WarningConstraint<A>> constraints) {
        this.constraints = constraints;
    }

    void fire(A input, Consumer<ConstraintResult> onWarning) {
        if (constraints.isEmpty()) return;
        Thread.startVirtualThread(() -> constraints.stream()
                .filter(c -> !c.isSatisfied(input))
                .map(c -> new ConstraintResult(Severity.WARNING, c.code()))
                .forEach(onWarning));
    }
}
