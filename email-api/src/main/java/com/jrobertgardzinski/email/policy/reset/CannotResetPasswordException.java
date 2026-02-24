package com.jrobertgardzinski.email.policy.reset;

import java.util.Collections;
import java.util.List;

public class CannotResetPasswordException extends RuntimeException {

    private final List<String> errors;

    public CannotResetPasswordException(Object subject, List<String> errors) {
        super(subject + " cannot reset password with " + errors.size() + " error(s): " + errors);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
