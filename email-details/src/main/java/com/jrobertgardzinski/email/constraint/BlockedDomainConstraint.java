package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

import java.util.Set;

public class BlockedDomainConstraint extends EmailConstraint {

    private final Set<String> blockedDomains;

    public BlockedDomainConstraint(Set<String> blockedDomains) {
        this.blockedDomains = Set.copyOf(blockedDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return !blockedDomains.contains(candidate.domain().value());
    }

    @Override
    public String errorMessage() {
        return "on the blocked domain list: " + blockedDomains;
    }
}
