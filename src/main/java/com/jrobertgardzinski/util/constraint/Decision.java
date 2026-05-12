package com.jrobertgardzinski.util.constraint;

import java.util.List;

/**
 * Outcome of evaluating a candidate against a set of {@link Constraints}.
 *
 * Three states are possible:
 * <ul>
 *   <li>{@link Allowed} — all error constraints satisfied, no warning raised.</li>
 *   <li>{@link AllowedWithWarning} — all error constraints satisfied, but a warning was raised.</li>
 *   <li>{@link Rejected} — at least one error constraint failed; lists all failed codes.</li>
 * </ul>
 */
public sealed interface Decision permits Decision.Allowed, Decision.AllowedWithWarning, Decision.Rejected {

    record Allowed() implements Decision {}

    record AllowedWithWarning(String warningCode) implements Decision {}

    record Rejected(List<String> errorCodes) implements Decision {}
}
