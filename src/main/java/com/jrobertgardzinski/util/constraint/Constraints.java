package com.jrobertgardzinski.util.constraint;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * A set of constraints applied to a candidate of type {@code T}.
 *
 * Composed of error constraints (any failure rejects the candidate) and
 * optional warning constraints (failures do not reject, but are reported).
 *
 * Replaces the recurring {@code stream().filter(...).map(code).toList()}
 * pattern by giving the operation a name and a return type: {@link #decide(Object)}.
 */
public final class Constraints<T> {

    private final List<? extends ErrorConstraint<T>> errors;
    private final List<? extends WarningConstraint<T>> warnings;

    public Constraints(List<? extends ErrorConstraint<T>> errors) {
        this(errors, List.of());
    }

    public Constraints(List<? extends ErrorConstraint<T>> errors, List<? extends WarningConstraint<T>> warnings) {
        this.errors = List.copyOf(errors);
        this.warnings = List.copyOf(warnings);
    }

    public Outcome<T> validate(Supplier<T> candidateSupplier) {
        T candidate;
        try {
            candidate = candidateSupplier.get();
        }
        catch (Exception e) {
            return new Outcome.RejectedDueToInvariantBreakage<>(
                    Collections.singletonList(e.getMessage()));
        }

        List<String> failedCodes = errors.stream()
                .filter(c -> !c.isSatisfied(candidate))
                .map(Constraint::code)
                .toList();

        if (!failedCodes.isEmpty()) {
            return new Outcome.Rejected<>(failedCodes);
        }
        List<String> warningCodes = warnings.stream()
                .filter(w -> !w.isSatisfied(candidate))
                .map(Constraint::code)
                .toList();
        if (!warningCodes.isEmpty()) {
            return new Outcome.AllowedWithWarning<>(candidate, warningCodes);
        }
        return new Outcome.Allowed<>(candidate);
    }
}
