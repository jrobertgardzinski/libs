package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

import java.util.Set;

public class DisposableEmailConstraint extends EmailConstraint {

    private final Set<String> disposableDomains;

    public DisposableEmailConstraint(Set<String> disposableDomains) {
        this.disposableDomains = Set.copyOf(disposableDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return !disposableDomains.contains(candidate.domain().value());
    }

    @Override
    public String errorMessage() {
        return "One of a disposable email domain: " + disposableDomains;
    }
}
