package com.jrobertgardzinski.util.constraint;

import java.util.List;
import java.util.function.Supplier;

/**
 * A set of constraints applied to a candidate of type {@code T}.
 *
 * Composed of error constraints (any failure rejects the candidate) and
 * optional warning constraints (failures do not reject, but are reported).
 *
 * Replaces the recurring {@code stream().filter(...).map(code).toList()}
 * pattern by giving the operation a name and a return type: {@link #validate(Supplier)}.
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

    /**
     * Builds the candidate, then judges it: error constraints first (any failure rejects), then
     * warnings (reported, never rejecting).
     *
     * <h4>What happens when the supplier throws</h4>
     * Any {@link RuntimeException} out of {@code candidateSupplier} becomes
     * {@link Outcome.RejectedDueToInvariantBreakage}, carrying one entry — the exception's message,
     * or its class's simple name when the message is absent or blank, so the list never holds
     * {@code null} next to the constraints' codes.
     *
     * <p><b>This machinery cannot tell a refused value from a broken collaborator.</b> It is
     * generic by design and knows no exception type of any product, so a value object rejecting a
     * malformed input and a port failing underneath it (a store that is down, a bug dereferencing
     * null) arrive here as the same {@code RuntimeException} and leave as the same outcome. A
     * caller that needs the two told apart catches its own exception type around its own supplier
     * and maps it to a code before handing the already-built candidate over — then this
     * {@code catch} is unreachable and the distinction lives where the vocabulary is known. That
     * is the pattern in {@code email-usecase}'s candidate builder, and the reason the translation
     * is not here.
     *
     * <p>A caller that does not do this reports an outage as a plain refusal. The known case is
     * the role gate in {@code security-system}, whose supplier reads the user store through a
     * port: it denies (the safe direction) but names the denial a broken invariant. Narrowing that
     * is the caller's decision, not the kernel's.
     */
    public Outcome<T> validate(Supplier<T> candidateSupplier) {
        T candidate;
        try {
            candidate = candidateSupplier.get();
        }
        catch (RuntimeException e) {
            return new Outcome.RejectedDueToInvariantBreakage<>(List.of(describe(e)));
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

    /** Never {@code null}: a message-less exception is described by its type. */
    private static String describe(RuntimeException e) {
        String message = e.getMessage();
        return message == null || message.isBlank() ? e.getClass().getSimpleName() : message;
    }
}
