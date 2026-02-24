package com.jrobertgardzinski.email.exception;

import java.util.Collections;
import java.util.List;

public class EmailValidationException extends RuntimeException {

    private final List<String> errors;

    public EmailValidationException(Object subject, List<String> errors) {
        super(subject + " validation failed with " + errors.size() + " error(s): " + errors);
        this.errors = errors;
    }

    public EmailValidationException(List<String> errors) {
        super("Email creation failed with " + errors.size() + " error(s): " + errors);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
