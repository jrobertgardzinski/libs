package com.jrobertgardzinski.email.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InvalidEmailException extends RuntimeException {

    private final List<String> errors;

    public InvalidEmailException(List<String> errors) {
        super("Email creation failed with " + errors.size() + " errors: " + errors);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
