package com.jrobertgardzinski.email.specification;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailSpecification;
import com.jrobertgardzinski.email.domain.ValidationResult;

public class MxRecordSpecification implements EmailSpecification {

    private final MxRecordPort mxRecordPort;

    public MxRecordSpecification(MxRecordPort mxRecordPort) {
        this.mxRecordPort = mxRecordPort;
    }

    @Override
    public ValidationResult validate(Email candidate) {
        if (!mxRecordPort.hasMxRecord(candidate)) {
            return ValidationResult.failure("The domain " + candidate.domain() + " has no MX record");
        }
        return ValidationResult.ok();
    }
}
