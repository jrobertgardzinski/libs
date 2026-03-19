package com.jrobertgardzinski.password.domain;

import java.util.Objects;

/**
 * Hashed password value object. Produced by a hash algorithm, never constructed directly.
 */
public final class PasswordHash {
    private final String value;

    public PasswordHash(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordHash that = (PasswordHash) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PasswordHash[value=" + value + "]";
    }
}
