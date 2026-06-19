package com.jrobertgardzinski.util.constraint;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public sealed interface Outcome<T> {

    default List<String> errorCodes() {
        return switch (this) {
            case Allowed<T> _, AllowedWithWarning<T> _ -> List.of();
            case Rejected<T> r -> r.errorCodes();
            case RejectedDueToInvariantBreakage<T> r -> r.errorCodes();
        };
    }

    default Optional<T> findValue() {
        return switch (this) {
            case Allowed<T> a            -> Optional.of(a.value());
            case AllowedWithWarning<T> w -> Optional.of(w.value());
            case Rejected<T> _           -> Optional.empty();
            case RejectedDueToInvariantBreakage<T> _ -> Optional.empty();
        };
    }

    default <R> Outcome<R> map(Function<? super T, ? extends R> mapper) {
        return switch (this) {
            case Allowed<T> a            -> new Allowed<>(mapper.apply(a.value()));
            case AllowedWithWarning<T> w -> new AllowedWithWarning<>(mapper.apply(w.value()), w.warningCodes());
            case Rejected<T> r           -> new Rejected<>(r.errorCodes());
            case RejectedDueToInvariantBreakage<T> r -> new RejectedDueToInvariantBreakage<>(r.errorCodes());
        };
    }

    record Allowed<T>(T value) implements Outcome<T> {}
    record AllowedWithWarning<T>(T value, List<String> warningCodes) implements Outcome<T> {}
    record Rejected<T>(List<String> errorCodes) implements Outcome<T> {}
    record RejectedDueToInvariantBreakage<T>(List<String> errorCodes) implements Outcome<T> {}
}