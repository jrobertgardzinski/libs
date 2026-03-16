package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.constraint.ConstraintResult;
import com.jrobertgardzinski.util.constraint.ErrorConstraint;
import com.jrobertgardzinski.util.constraint.Severity;
import com.jrobertgardzinski.util.constraint.WarningConstraint;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanRegister {

    private final List<ErrorConstraint<Email>> blockingConstraints;
    private final List<WarningConstraint<Email>> warningConstraints;
    private final Consumer<List<ConstraintResult>> warningHandler;

    private CanRegister(
            List<ErrorConstraint<Email>> blockingConstraints,
            List<WarningConstraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        this.blockingConstraints = blockingConstraints;
        this.warningConstraints = warningConstraints;
        this.warningHandler = warningHandler;
    }

    public RegistrationDecision evaluate(Email email) {
        var violations = blockingConstraints.stream()
                .filter(c -> !c.isSatisfied(email))
                .map(c -> new ConstraintResult(Severity.BLOCKING, c.code()))
                .toList();
        if (!violations.isEmpty()) {
            return RegistrationDecision.rejected(violations);
        }
        fireWarningsAsync(email);
        return RegistrationDecision.allowed();
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

    public static CanRegister minimalistic() {
        return new CanRegister(
                List.of(new _RfcFormatConstraint()),
                List.of(),
                null);
    }

    public static CanRegister maximalistic(
            Set<String> disposableDomains,
            Set<String> blockedDomains,
            Set<String> companyDomains,
            MxRecordPort mxRecordPort,
            Consumer<List<ConstraintResult>> warningHandler) {
        return new CanRegister(
                List.of(
                        new _RfcFormatConstraint(),
                        new _DisposableEmailConstraint(disposableDomains),
                        new _BlockedDomainConstraint(blockedDomains),
                        new _IsEmployeeConstraint(companyDomains)),
                List.of(new _MxRecordConstraint(mxRecordPort)),
                warningHandler);
    }

    public static CanRegister custom(
            List<ErrorConstraint<Email>> blockingConstraints,
            List<WarningConstraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        return new CanRegister(
                List.copyOf(blockingConstraints),
                List.copyOf(warningConstraints),
                warningHandler);
    }
}
