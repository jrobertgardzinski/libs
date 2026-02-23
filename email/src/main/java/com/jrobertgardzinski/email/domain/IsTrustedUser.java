package com.jrobertgardzinski.email.domain;

import java.util.Set;

public class IsTrustedUser implements EmailPolicy {

    private final Set<String> whitelistedAddresses;
    private final Set<String> trustedDomains;

    public IsTrustedUser(Set<String> whitelistedAddresses, Set<String> trustedDomains) {
        this.whitelistedAddresses = Set.copyOf(whitelistedAddresses);
        this.trustedDomains       = Set.copyOf(trustedDomains);
    }

    @Override
    public ValidationResult validate(Email email) {
        return whitelistedAddresses.contains(email.value()) || trustedDomains.contains(email.domain().value())
                ? ValidationResult.ok()
                : ValidationResult.failure("email is not from a trusted source");
    }
}
