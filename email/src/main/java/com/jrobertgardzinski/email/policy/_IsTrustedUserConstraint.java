package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;

import java.util.Set;

class _IsTrustedUserConstraint extends Constraint<Email> {

    private final Set<String> whitelistedAddresses;
    private final Set<String> trustedDomains;

    _IsTrustedUserConstraint(Set<String> whitelistedAddresses, Set<String> trustedDomains) {
        this.whitelistedAddresses = Set.copyOf(whitelistedAddresses);
        this.trustedDomains       = Set.copyOf(trustedDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return whitelistedAddresses.contains(candidate.value()) ||
                trustedDomains.contains(candidate.domain().value());
    }

    @Override
    public String errorMessage() {
        return "email is not from a trusted source";
    }
}
