package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

class _EvaluatePolicy {

    private _EvaluatePolicy() {}

    static <A, D> void run(
            A input,
            _FastPhase<A> fast,
            _WarningPhase<A> warning,
            Supplier<D> allowed,
            Function<List<ConstraintResult>, D> rejected,
            Consumer<PolicyEvent<D>> observer) {
        var v = fast.evaluate(input);
        if (v.isPresent()) {
            observer.accept(new PolicyEvent.Violation<>(v.get()));
            observer.accept(new PolicyEvent.Outcome<>(rejected.apply(List.of(v.get()))));
            return;
        }
        observer.accept(new PolicyEvent.Outcome<>(allowed.get()));
        warning.fire(input, w -> observer.accept(new PolicyEvent.Warning<>(w)));
    }
}
