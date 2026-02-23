package com.jrobertgardzinski.email.domain;

// todo policy is not the same as specification!
@FunctionalInterface
public interface EmailSpecification extends EmailPolicy {

    // SAM: ValidationResult validate(Email email) — inherited from EmailPolicy

    default boolean isSatisfiedBy(Email candidate) {
        return validate(candidate).isValid();
    }

    default EmailSpecification and(EmailSpecification other) {
        return candidate -> this.validate(candidate).merge(other.validate(candidate));
    }

    default EmailSpecification or(EmailSpecification other) {
        return candidate -> {
            ValidationResult r = this.validate(candidate);
            return r.isValid() ? r : other.validate(candidate);
        };
    }

    default EmailSpecification negate() {
        return candidate -> isSatisfiedBy(candidate)
                ? ValidationResult.failure("negation of specification failed")
                : ValidationResult.ok();
    }
}
