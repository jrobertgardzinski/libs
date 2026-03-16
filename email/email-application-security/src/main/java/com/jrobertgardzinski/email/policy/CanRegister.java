package com.jrobertgardzinski.email.policy;

import com.jrobertgardzinski.email.domain.Email;
import com.jrobertgardzinski.email.external.MxRecordPort;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CanRegister {

    private final _FastPhase<Email> fastPhase;
    private final _WarningPhase<Email> warningPhase;

    private CanRegister(_FastPhase<Email> fastPhase, _WarningPhase<Email> warningPhase) {
        this.fastPhase = fastPhase;
        this.warningPhase = warningPhase;
    }

    public void evaluate(Email email, Consumer<PolicyEvent<RegistrationDecision>> observer) {
        _EvaluatePolicy.run(email, fastPhase, warningPhase,
                RegistrationDecision::allowed, RegistrationDecision::rejected, observer);
    }

    public static CanRegister create(
            Set<String> disposableDomains,
            Set<String> blockedDomains,
            Set<String> companyDomains,
            MxRecordPort mxRecordPort) {
        return new CanRegister(
                new _FastPhase<>(List.of(
                        new _RfcFormatConstraint(),
                        new _DisposableEmailConstraint(disposableDomains),
                        new _BlockedDomainConstraint(blockedDomains),
                        new _IsEmployeeConstraint(companyDomains))),
                new _WarningPhase<>(List.of(new _MxRecordConstraint(mxRecordPort))));
    }
}
