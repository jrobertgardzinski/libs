package com.jrobertgardzinski.email.domain;

import com.jrobertgardzinski.email.specification.BlockedDomainSpecification;
import com.jrobertgardzinski.email.specification.DisposableEmailSpecification;
import com.jrobertgardzinski.email.specification.RfcFormatSpecification;

import java.util.Set;

public class CanRegister implements EmailPolicy {

    private final EmailSpecification policy;

    public CanRegister(Set<String> disposableDomains, Set<String> blockedDomains) {
        this.policy = new RfcFormatSpecification()
                .and(new DisposableEmailSpecification(disposableDomains))
                .and(new BlockedDomainSpecification(blockedDomains));
    }

    @Override
    public ValidationResult validate(Email email) {
        return policy.validate(email);
    }
}
