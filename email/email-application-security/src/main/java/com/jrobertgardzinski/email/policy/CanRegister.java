package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;
import com.jrobertgardzinski.util.Constraint;
import com.jrobertgardzinski.util.ConstraintResult;
import com.jrobertgardzinski.util.Decision;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanRegister {

    private final _RegistrationEvaluator evaluator;

    private CanRegister(
            List<Constraint<Email>> blockingConstraints,
            List<Constraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        this.evaluator = new _RegistrationEvaluator(blockingConstraints, warningConstraints, warningHandler);
    }

    public RegistrationDecision evaluate(Email email) {
        var blockingViolations = evaluator.evaluateBlocking(email);
        if (!blockingViolations.isEmpty()) {
            return new RegistrationDecision(Decision.REJECTED, blockingViolations);
        }

        evaluator.fireWarningsAsync(email);

        return new RegistrationDecision(Decision.ALLOWED, List.of());
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
            List<Constraint<Email>> blockingConstraints,
            List<Constraint<Email>> warningConstraints,
            Consumer<List<ConstraintResult>> warningHandler) {
        return new CanRegister(
                List.copyOf(blockingConstraints),
                List.copyOf(warningConstraints),
                warningHandler);
    }
}
