package com.jrobertgardzinski.password.policy;

import java.util.Objects;

/**
 * Immutable configuration class for password policy.
 * All defaults live in the Builder — one place to change them.
 */
public final class PasswordPolicyConfig {
    private final int minLength;
    private final boolean requireLowercase;
    private final boolean requireUppercase;
    private final boolean requireDigit;
    private final String specialChars;

    public PasswordPolicyConfig(
            int minLength,
            boolean requireLowercase,
            boolean requireUppercase,
            boolean requireDigit,
            String specialChars
    ) {
        this.minLength = minLength;
        this.requireLowercase = requireLowercase;
        this.requireUppercase = requireUppercase;
        this.requireDigit = requireDigit;
        this.specialChars = specialChars;
    }

    public int minLength() { return minLength; }
    public boolean requireLowercase() { return requireLowercase; }
    public boolean requireUppercase() { return requireUppercase; }
    public boolean requireDigit() { return requireDigit; }
    public String specialChars() { return specialChars; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordPolicyConfig that = (PasswordPolicyConfig) o;
        return minLength == that.minLength && 
                requireLowercase == that.requireLowercase && 
                requireUppercase == that.requireUppercase && 
                requireDigit == that.requireDigit && 
                Objects.equals(specialChars, that.specialChars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLength, requireLowercase, requireUppercase, requireDigit, specialChars);
    }

    @Override
    public String toString() {
        return "PasswordPolicyConfig[" +
                "minLength=" + minLength +
                ", requireLowercase=" + requireLowercase +
                ", requireUppercase=" + requireUppercase +
                ", requireDigit=" + requireDigit +
                ", specialChars='" + specialChars + '\'' +
                ']';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int minLength = 12;
        private boolean requireLowercase = true;
        private boolean requireUppercase = true;
        private boolean requireDigit = true;
        private String specialChars = "#?!";

        public Builder minLength(int minLength) {
            this.minLength = minLength;
            return this;
        }

        public Builder requireLowercase(boolean requireLowercase) {
            this.requireLowercase = requireLowercase;
            return this;
        }

        public Builder requireUppercase(boolean requireUppercase) {
            this.requireUppercase = requireUppercase;
            return this;
        }

        public Builder requireDigit(boolean requireDigit) {
            this.requireDigit = requireDigit;
            return this;
        }

        public Builder specialChars(String specialChars) {
            this.specialChars = specialChars;
            return this;
        }

        public Builder noSpecialChars() {
            this.specialChars = "";
            return this;
        }

        public PasswordPolicyConfig build() {
            return new PasswordPolicyConfig(minLength, requireLowercase, requireUppercase, requireDigit, specialChars);
        }
    }
}
