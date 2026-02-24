package com.jrobertgardzinski.email.policy.reset;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.domain.EmailConstraint;
import com.jrobertgardzinski.util.Validate;

import java.util.List;
import java.util.function.Consumer;

public class CanResetPassword implements Consumer<Email> {

    private final List<EmailConstraint> constraints;

    public CanResetPassword(List<EmailConstraint> constraints) {
        this.constraints = List.copyOf(constraints);
    }

    @Override
    public void accept(Email email) {
        Validate.throwOnErrors(email, constraints, CannotResetPasswordException::new);
    }
}
