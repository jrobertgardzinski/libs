package com.jrobertgardzinski.password.domain;

import java.util.Objects;

/**
 * Plaintext password value object.
 * Enforces only that the value is not blank — strength rules live in CanSetPassword.
 * Never exposes the raw value in toString() to prevent accidental logging.
 */
public final class PlaintextPassword {

    private final String value;

    private PlaintextPassword(String value) {
        this.value = value;
    }

    public static PlaintextPassword of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        return new PlaintextPassword(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaintextPassword)) return false;
        PlaintextPassword other = (PlaintextPassword) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    /** Redacted intentionally — never log plaintext passwords. */
    @Override
    public String toString() {
        return "PlaintextPassword[value=REDACTED]";
    }
}
