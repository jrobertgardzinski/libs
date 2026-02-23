package com.jrobertgardzinski.email.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public final class ValidationResult {

    private final Optional<String> violation;

    private ValidationResult(String violation) {
        this.violation = Optional.ofNullable(violation);
    }

    public static ValidationResult ok() {
        return new ValidationResult(null);
    }

    public static ValidationResult failure(String violation) {
        return new ValidationResult(violation);
    }

    public boolean isValid() {
        return violation.isEmpty();
    }

    public void ifInvalid(Consumer<String> consumer) {
        violation.ifPresent(consumer);
    }
}
