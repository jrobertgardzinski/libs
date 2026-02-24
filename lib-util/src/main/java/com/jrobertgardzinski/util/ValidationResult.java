package com.jrobertgardzinski.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class ValidationResult {

    private final List<String> violations;

    private ValidationResult(List<String> violations) {
        this.violations = Collections.unmodifiableList(new ArrayList<>(violations));
    }

    public static ValidationResult ok() {
        return new ValidationResult(List.of());
    }

    public static ValidationResult failure(String violation) {
        return new ValidationResult(List.of(violation));
    }

    public boolean isValid() {
        return violations.isEmpty();
    }

    public void ifInvalid(Consumer<String> consumer) {
        violations.forEach(consumer);
    }

    public List<String> violations() {
        return violations;
    }

    public ValidationResult and(ValidationResult other) {
        List<String> all = new ArrayList<>(this.violations);
        all.addAll(other.violations);
        return new ValidationResult(all);
    }
}
