package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.Severity;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanResetPassword {

    private final List<ErrorConstraint<Email>> blockingConstraints;
    private final List<WarningConstraint<Email>> warningConstraints;
    private final Consumer<List<ConstraintResult>> warningHandler;

    private CanResetPassword(
            List<ErrorConstraint<Email>> blockingConstraints,
            List<WarningConstraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        this.blockingConstraints = blockingConstraints;
        this.warningConstraints = warningConstraints;
        this.warningHandler = warningHandler;
    }

    public PasswordResetDecision evaluate(Email email) {
        var violations = blockingConstraints.stream()
                .filter(c -> !c.isSatisfied(email))
                .map(c -> new ConstraintResult(Severity.BLOCKING, c.code()))
                .toList();
        if (!violations.isEmpty()) {
            return PasswordResetDecision.rejected(violations);
        }
        fireWarningsAsync(email);
        return PasswordResetDecision.allowed();
    }

    private void fireWarningsAsync(Email email) {
        if (warningConstraints.isEmpty() || warningHandler == null) return;
        Thread.startVirtualThread(() -> {
            var warnings = warningConstraints.stream()
                    .filter(c -> !c.isSatisfied(email))
                    .map(c -> new ConstraintResult(Severity.WARNING, c.code()))
                    .toList();
            if (!warnings.isEmpty()) warningHandler.accept(warnings);
        });
    }

    public static CanResetPassword minimalistic() {
        return new CanResetPassword(
                List.of(new _RfcFormatConstraint()),
                List.of(),
                null);
    }

    public static CanResetPassword maximalistic(
            Set<String> blockedDomains,
            Consumer<List<ConstraintResult>> warningHandler) {
        return new CanResetPassword(
                List.of(
                        new _RfcFormatConstraint(),
                        new _BlockedDomainConstraint(blockedDomains)),
                List.of(),
                warningHandler);
    }

    public static CanResetPassword custom(
            List<ErrorConstraint<Email>> blockingConstraints,
            List<WarningConstraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        return new CanResetPassword(
                List.copyOf(blockingConstraints),
                List.copyOf(warningConstraints),
                warningHandler);
    }
}
