package com.jrobertgardzinski.email.constraint;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;

public class MxRecordConstraint extends EmailConstraint {

    private final MxRecordPort mxRecordPort;

    public MxRecordConstraint(MxRecordPort mxRecordPort) {
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
