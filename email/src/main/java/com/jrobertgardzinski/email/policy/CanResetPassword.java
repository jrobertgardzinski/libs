package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.Validate;

import java.util.List;
import java.util.function.Consumer;

public class CanResetPassword implements Consumer<Email> {

    private final List<Constraint<Email>> constraints;

    public CanResetPassword(MxRecordPort mxRecordPort) {
        this.constraints = List.of(new _MxRecordConstraint(mxRecordPort));
    }

    @Override
    public void accept(Email email) {
        Validate.throwOnErrors(email, constraints, CannotResetPasswordException::new);
    }
}
