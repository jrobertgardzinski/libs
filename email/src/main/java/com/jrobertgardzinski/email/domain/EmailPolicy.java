package com.jrobertgardzinski.email.domain;

public interface EmailPolicy {

    ValidationResult validate(Email email);
}
