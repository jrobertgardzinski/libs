package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.Constraint;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CanSetPassword {

    private final List<ErrorConstraint<PlaintextPassword>> constraints;

    public CanSetPassword() {
        this(Config.builder().build());
    }

    public CanSetPassword(Config config) {
        List<ErrorConstraint<PlaintextPassword>> list = new ArrayList<>();
        list.add(new _MinLengthConstraint(config.minLength()));
        if (config.requireLowercase()) list.add(new _ContainsLowercaseConstraint());
        if (config.requireUppercase()) list.add(new _ContainsUppercaseConstraint());
        if (config.requireDigit()) list.add(new _ContainsDigitConstraint());
        String specialChars = config.specialChars();
        if (specialChars != null && !specialChars.isBlank()) {
            list.add(new _ContainsSpecialCharConstraint(specialChars));
        }
        this.constraints = List.copyOf(list);
    }

    public Decision evaluate(PlaintextPassword password) {
        List<String> codes = constraints.stream()
                .filter(c -> !c.isSatisfied(password))
                .map(Constraint::code)
                .toList();
        return codes.isEmpty() ? new Decision.Allowed() : new Decision.Rejected(codes);
    }

    public interface Decision {
        record Rejected(List<String> errorCodes) implements Decision {}
        record Allowed() implements Decision {}
    }

    public static final class Config {
        private final int minLength;
        private final boolean requireLowercase;
        private final boolean requireUppercase;
        private final boolean requireDigit;
        private final String specialChars;

        private Config(int minLength, boolean requireLowercase, boolean requireUppercase,
                       boolean requireDigit, String specialChars) {
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
            Config that = (Config) o;
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
            return "CanSetPassword.Config[minLength=" + minLength +
                    ", requireLowercase=" + requireLowercase +
                    ", requireUppercase=" + requireUppercase +
                    ", requireDigit=" + requireDigit +
                    ", specialChars='" + specialChars + "']";
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

            public Config build() {
                return new Config(minLength, requireLowercase, requireUppercase, requireDigit, specialChars);
            }
        }
    }
}
