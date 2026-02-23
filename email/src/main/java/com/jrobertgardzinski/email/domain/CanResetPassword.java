package com.jrobertgardzinski.email.domain;

import com.jrobertgardzinski.email.specification.RfcFormatSpecification;

public class CanResetPassword implements EmailPolicy {

    private final EmailSpecification policy = new RfcFormatSpecification();

    @Override
    public ValidationResult validate(Email email) {
        return policy.validate(email);
    }
}
