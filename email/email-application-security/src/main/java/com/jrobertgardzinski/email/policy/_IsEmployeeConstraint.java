package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.Set;

class _IsEmployeeConstraint extends ErrorConstraint<Email> {

    private final Set<String> companyDomains;

    _IsEmployeeConstraint(Set<String> companyDomains) {
        this.companyDomains = Set.copyOf(companyDomains);
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return companyDomains.isEmpty() || companyDomains.contains(candidate.domain().value());
    }

    @Override
    public String code() {
        return "NOT_A_COMPANY_DOMAIN";
    }
}
