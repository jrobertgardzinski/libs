package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

import java.util.Set;

public class IsTrustedUserConstraint extends EmailConstraint {

    private final Set<String> whitelistedAddresses;
    private final Set<String> trustedDomains;

    public IsTrustedUserConstraint(Set<String> whitelistedAddresses, Set<String> trustedDomains) {
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
