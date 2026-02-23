package com.jrobertgardzinski.email.specification;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailSpecification;
import com.jrobertgardzinski.email.domain.ValidationResult;

import java.util.Set;

public class DisposableEmailSpecification implements EmailSpecification {

    private final Set<String> disposableDomains;

    public DisposableEmailSpecification(Set<String> disposableDomains) {
        this.disposableDomains = Set.copyOf(disposableDomains);
    }

    @Override
    public ValidationResult validate(Email candidate) {
        if (disposableDomains.contains(candidate.domain().value())) {
            return ValidationResult.failure("The email " + candidate + " uses a disposable email domain: " + candidate.domain());
        }
        return ValidationResult.ok();
    }
}
