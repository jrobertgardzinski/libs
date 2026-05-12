package com.jrobertgardzinski.util.constraint;

import java.util.List;

/**
 * A set of constraints applied to a candidate of type {@code T}.
 *
 * Composed of error constraints (any failure rejects the candidate) and an
 * optional warning constraint (failure does not reject, but is reported).
 *
 * Replaces the recurring {@code stream().filter(...).map(code).toList()}
 * pattern by giving the operation a name and a return type: {@link #decide(Object)}.
 */
public final class Constraints<T> {

    private final List<? extends ErrorConstraint<T>> errors;
    private final WarningConstraint<T> warning;

    public Constraints(List<? extends ErrorConstraint<T>> errors) {
        this(errors, null);
    }

    public Constraints(List<? extends ErrorConstraint<T>> errors, WarningConstraint<T> warning) {
        this.errors = List.copyOf(errors);
        this.warning = warning;
    }

    public Decision decide(T candidate) {
        List<String> failedCodes = errors.stream()
                .filter(c -> !c.isSatisfied(candidate))
                .map(Constraint::code)
                .toList();

        if (!failedCodes.isEmpty()) {
            return new Decision.Rejected(failedCodes);
        }
        if (warning != null && !warning.isSatisfied(candidate)) {
            return new Decision.AllowedWithWarning(warning.code());
        }
        return new Decision.Allowed();
    }
}
