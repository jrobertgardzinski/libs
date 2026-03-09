package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;

import java.util.Set;

class _IsEmployeeConstraint extends Constraint<Email> {

    private final Set<String> companyDomains;

    _IsEmployeeConstraint(Set<String> companyDomains) {
        this.companyDomains = Set.copyOf(companyDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return companyDomains.contains(candidate.domain().value());
    }

    @Override
    public String errorMessage() {
        return "email domain is not a company domain";
    }
}
