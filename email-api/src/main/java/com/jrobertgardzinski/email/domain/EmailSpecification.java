package com.jrobertgardzinski.email.domain;

import com.jrobertgardzinski.util.ValidationResult;

@FunctionalInterface
public interface EmailSpecification {

    ValidationResult validate(Email email);

    default EmailSpecification and(EmailSpecification other) {
        return email -> this.validate(email).and(other.validate(email));
    }
}
