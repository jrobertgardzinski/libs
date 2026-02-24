package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

import java.util.Set;

public class IsEmployeeConstraint extends EmailConstraint {

    private final Set<String> companyDomains;

    public IsEmployeeConstraint(Set<String> companyDomains) {
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
