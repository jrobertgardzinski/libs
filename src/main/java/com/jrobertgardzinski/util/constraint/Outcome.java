package com.jrobertgardzinski.util.constraint;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Result of a use-case backed by a {@link Decision}, carrying a payload on the success path.
 *
 * Three states, mirroring {@link Decision}:
 * <ul>
 *   <li>{@link Allowed} — positive decision, payload produced.</li>
 *   <li>{@link AllowedWithWarning} — positive decision with warning codes, payload produced.</li>
 *   <li>{@link Rejected} — negative decision; lists all failed error codes.</li>
 * </ul>
 */
public sealed interface Outcome<T> {

    default List<String> errorCodes() {
        return switch (this) {
            case Outcome.Allowed<T> _, Outcome.AllowedWithWarning<T> _ -> List.of();
            case Outcome.Rejected<T> r -> r.errorCodes();
        };
    }

    default Optional<T> findValue() {
        return switch (this) {
            case Allowed<T> a            -> Optional.of(a.value());
            case AllowedWithWarning<T> w -> Optional.of(w.value());
            case Rejected<T> r           -> Optional.empty();
        };
    }

    record Allowed<T>(T value) implements Outcome<T> {}

    record AllowedWithWarning<T>(T value, List<String> warningCodes) implements Outcome<T> {}

    record Rejected<T>(List<String> errorCodes) implements Outcome<T> {}

    static <T> Outcome<T> from(Decision<T> decision, Supplier<T> ifAllowed) {
        return switch (decision) {
            case Decision.Allowed<T> a            -> new Allowed<>(ifAllowed.get());
            case Decision.AllowedWithWarning<T> w -> new AllowedWithWarning<>(ifAllowed.get(), w.warningCodes());
            case Decision.Rejected<T> r           -> new Rejected<>(r.errorCodes());
        };
    }
}
