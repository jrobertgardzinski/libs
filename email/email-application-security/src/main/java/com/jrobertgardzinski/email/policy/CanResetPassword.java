package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanResetPassword {

    private final _FastPhase<Email> fastPhase;
    private final _WarningPhase<Email> warningPhase;

    private CanResetPassword(_FastPhase<Email> fastPhase, _WarningPhase<Email> warningPhase) {
        this.fastPhase = fastPhase;
        this.warningPhase = warningPhase;
    }

    public void evaluate(Email email, Consumer<PolicyEvent<PasswordResetDecision>> observer) {
        _EvaluatePolicy.run(email, fastPhase, warningPhase,
                PasswordResetDecision::allowed, PasswordResetDecision::rejected, observer);
    }

    public static CanResetPassword create(Set<String> blockedDomains) {
        return new CanResetPassword(
                new _FastPhase<>(List.of(
                        new _RfcFormatConstraint(),
                        new _BlockedDomainConstraint(blockedDomains))),
                new _WarningPhase<>(List.of()));
    }
}
