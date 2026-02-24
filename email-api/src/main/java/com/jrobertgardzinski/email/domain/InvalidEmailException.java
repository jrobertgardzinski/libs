package com.jrobertgardzinski.email.domain;

import java.util.Collections;
import java.util.List;

public class InvalidEmailException extends RuntimeException {

    private final List<String> errors;

    public InvalidEmailException(Email email, List<String> errors) {
        super(email + " creation failed with " + errors.size() + " error(s): " + errors);
        this.errors = errors;
    }

    public InvalidEmailException(List<String> errors) {
        super("Email creation failed with " + errors.size() + " error(s): " + errors);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
