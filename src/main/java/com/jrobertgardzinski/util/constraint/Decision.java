package com.jrobertgardzinski.util.constraint;

import java.util.List;

/**
 * Outcome of evaluating a candidate of type {@code T} against a set of {@link Constraints}.
 *
 * The type parameter is phantom — it carries no value at runtime but lets call
 * sites tell apart decisions on different candidate types (e.g. {@code Decision<Email>}
 * vs {@code Decision<PlaintextPassword>}) and prevents accidentally mixing them up.
 *
 * Three states are possible:
 * <ul>
 *   <li>{@link Allowed} — all error constraints satisfied, no warning raised.</li>
 *   <li>{@link AllowedWithWarning} — all error constraints satisfied, but at least one warning was raised.</li>
 *   <li>{@link Rejected} — at least one error constraint failed; lists all failed codes.</li>
 * </ul>
 */
public sealed interface Decision<T> permits Decision.Allowed, Decision.AllowedWithWarning, Decision.Rejected {

    default List<String> errorCodes() {
        return switch (this) {
            case Allowed<T> _, AllowedWithWarning<T> _ -> List.of();
            case Rejected<T> r -> r.errorCodes();
        };
    }

    record Allowed<T>() implements Decision<T> {}

    record AllowedWithWarning<T>(List<String> warningCodes) implements Decision<T> {}

    record Rejected<T>(List<String> errorCodes) implements Decision<T> {}
}
