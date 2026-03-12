package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;

import java.util.Set;

class _BlockedDomainConstraint extends Constraint<Email> {

    private final Set<String> blockedDomains;

    _BlockedDomainConstraint(Set<String> blockedDomains) {
        this.blockedDomains = Set.copyOf(blockedDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return !blockedDomains.contains(candidate.domain().value());
    }

    @Override
    public String code() {
        return "DOMAIN_BLOCKED";
    }
}
