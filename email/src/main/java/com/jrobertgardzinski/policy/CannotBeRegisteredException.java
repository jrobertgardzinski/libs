package com.jrobertgardzinski.policy;

import java.util.Collections;
import java.util.List;

public class CannotBeRegisteredException extends RuntimeException {

    private final List<String> errors;

    public CannotBeRegisteredException(Object subject, List<String> errors) {
        super(subject + " cannot be registered with " + errors.size() + " error(s): " + errors);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
