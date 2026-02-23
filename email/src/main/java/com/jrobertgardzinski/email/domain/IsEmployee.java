package com.jrobertgardzinski.email.domain;

import java.util.Set;

public class IsEmployee implements EmailPolicy {

    private final Set<String> companyDomains;

    public IsEmployee(Set<String> companyDomains) {
        this.companyDomains = Set.copyOf(companyDomains);
    }

    @Override
    public ValidationResult validate(Email email) {
        return companyDomains.contains(email.domain().value())
                ? ValidationResult.ok()
                : ValidationResult.failure("email domain is not a company domain");
    }
}
