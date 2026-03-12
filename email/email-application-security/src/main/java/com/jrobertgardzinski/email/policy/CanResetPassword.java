package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.ConstraintResult;
import com.jrobertgardzinski.util.Decision;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanResetPassword {

    private final _PasswordResetEvaluator evaluator;

    private CanResetPassword(
            List<Constraint<Email>> blockingConstraints,
            List<Constraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        this.evaluator = new _PasswordResetEvaluator(blockingConstraints, warningConstraints, warningHandler);
    }

    public PasswordResetDecision evaluate(Email email) {
        var blockingViolations = evaluator.evaluateBlocking(email);
        if (!blockingViolations.isEmpty()) {
            return new PasswordResetDecision(Decision.REJECTED, blockingViolations);
        }

        evaluator.fireWarningsAsync(email);

        return new PasswordResetDecision(Decision.ALLOWED, List.of());
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
            List<Constraint<Email>> blockingConstraints,
            List<Constraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        return new CanResetPassword(
                List.copyOf(blockingConstraints),
                List.copyOf(warningConstraints),
                warningHandler);
    }
}
