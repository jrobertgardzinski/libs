package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.Constraint;

class _MxRecordConstraint extends Constraint<Email> {

    private final MxRecordPort mxRecordPort;

    _MxRecordConstraint(MxRecordPort mxRecordPort) {
        this.mxRecordPort = mxRecordPort;
    }

    @Override
    public boolean isSatisfied(Email candidate) {
        return mxRecordPort.hasMxRecord(candidate);
    }

    @Override
    public String errorMessage() {
        return "no MX record";
    }
}
