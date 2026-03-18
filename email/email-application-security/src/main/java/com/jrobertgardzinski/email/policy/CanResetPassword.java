package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.Constraint;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;

import java.util.List;
import java.util.Set;

public class CanResetPassword {

    private final List<ErrorConstraint<Email>> errorConstraints;

    public CanResetPassword(Set<String> blockedDomains) {
        this.errorConstraints = List.of(
                new _RfcFormatConstraint(),
                new _BlockedDomainConstraint(blockedDomains)
        );
    }

    public Decision evaluate(Email email) {
        List<String> codes = errorConstraints.stream()
                .filter(el -> !el.isSatisfied(email))
                .map(Constraint::code)
                .toList();
        return codes.isEmpty() ?
                new Decision.Allowed() :
                new Decision.Rejected(codes);
    }

    public interface Decision {
        record Rejected(
                List<String> errorCodes) implements Decision {
        }

        record Allowed() implements Decision {}
    }

}
