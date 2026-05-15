package com.jrobertgardzinski.util.constraint;

import java.util.List;

/**
 * Outcome of evaluating a candidate against a set of {@link Constraints}.
 *
 * Three states are possible:
 * <ul>
 *   <li>{@link Allowed} — all error constraints satisfied, no warning raised.</li>
 *   <li>{@link AllowedWithWarning} — all error constraints satisfied, but at least one warning was raised.</li>
 *   <li>{@link Rejected} — at least one error constraint failed; lists all failed codes.</li>
 * </ul>
 */
public sealed interface Decision permits Decision.Allowed, Decision.AllowedWithWarning, Decision.Rejected {

    default List<String> errorCodes() {
        return switch (this) {
            case Allowed _, AllowedWithWarning _ -> List.of();
            case Rejected r -> r.errorCodes();
        };
    }

    record Allowed() implements Decision {}

    record AllowedWithWarning(List<String> warningCodes) implements Decision {}

    record Rejected(List<String> errorCodes) implements Decision {}
}
