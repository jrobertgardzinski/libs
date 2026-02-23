package com.jrobertgardzinski.email.specification;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailSpecification;
import com.jrobertgardzinski.email.domain.ValidationResult;

import java.util.Set;

public class BlockedDomainSpecification implements EmailSpecification {

    private final Set<String> blockedDomains;

    public BlockedDomainSpecification(Set<String> blockedDomains) {
        this.blockedDomains = Set.copyOf(blockedDomains);
    }

    @Override
    public ValidationResult validate(Email candidate) {
        if (blockedDomains.contains(candidate.domain().value())) {
            return ValidationResult.failure("The email " + candidate + " is on the blocked domain list: " + blockedDomains);
        }
        return ValidationResult.ok();
    }
}
