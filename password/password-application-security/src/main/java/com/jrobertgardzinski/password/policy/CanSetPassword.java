package com.jrobertgardzinski.password.policy;

import com.jrobertgardzinski.password.domain.PlaintextPassword;
import com.jrobertgardzinski.util.constraint.Constraint;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.ArrayList;
import java.util.List;

public class CanSetPassword {

    private final List<ErrorConstraint<PlaintextPassword>> constraints;

    public CanSetPassword() {
        this(PasswordPolicyConfig.builder().build());
    }

    public CanSetPassword(PasswordPolicyConfig config) {
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
}
