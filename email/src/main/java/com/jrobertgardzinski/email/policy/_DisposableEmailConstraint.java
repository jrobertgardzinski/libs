package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;

import java.util.Set;

class _DisposableEmailConstraint extends Constraint<Email> {

    private final Set<String> disposableDomains;

    _DisposableEmailConstraint(Set<String> disposableDomains) {
        this.disposableDomains = Set.copyOf(disposableDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return !disposableDomains.contains(candidate.domain().value());
    }

    @Override
    public String code() {
        return "DISPOSABLE_DOMAIN";
    }
}
